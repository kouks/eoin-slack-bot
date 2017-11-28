package io.pavelkoch.eoin.rtm.dispatchers;

import io.pavelkoch.eoin.modules.Module;
import io.pavelkoch.eoin.rtm.Dialogue;
import io.pavelkoch.eoin.rtm.Dispatcher;
import io.pavelkoch.eoin.rtm.Event;
import io.pavelkoch.eoin.rtm.EventType;
import io.pavelkoch.eoin.rtm.conversations.ConversationStore;
import io.pavelkoch.eoin.rtm.events.Message;
import io.pavelkoch.eoin.rtm.messaging.Response;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConversationDispatcher implements Dispatcher {
    /**
     * The message event implementation that is being dispatched.
     */
    private Message event;

    /**
     * Loops through all available modules and dispatches events to suitable conversations
     * on those modules.
     *
     * @param eventType The dispatched event type
     * @param event The dispatched event implementation
     * @param modules All available modules to be dispatched to
     */
    @Override
    public void dispatch(EventType eventType, Event event, ArrayList<Module> modules) {
        // Conversations are only suitable for message events, that is why
        // we need to eliminate all other events.
        if (eventType != EventType.MESSAGE) {
            return;
        }

        // Now we can safely cast our event implementation to message
        // since we previously eliminated all other ones.
        this.event = (Message) event;

        for (Module module : modules) {
            Map<String, Method> conversationMethods = this.findConversationMethodsForModule(module);

            if (conversationMethods.size() > 1) {
                this.dispatchConversation(module, conversationMethods);
            }
        }
    }

    /**
     * Finds all methods on a provided module that are annotated as conversation.
     *
     * @param module The module that we are searching
     * @return An array list of all conversation-annotated methods
     */
    private Map<String, Method> findConversationMethodsForModule(Module module) {
        Map<String, Method> conversationMethods = new HashMap<>();

        for (Method method : module.getClass().getMethods()) {
            if (method.getAnnotation(Dialogue.class) != null) {
                conversationMethods.put(method.getName(), method);
            }
        }

        return conversationMethods;
    }

    /**
     * Dispatching the conversation by finding a current one or creating a new one - based
     * on user ID. Then we find the appropriated method and invoke it.
     *
     * @param module The module on which the conversation is present
     * @param conversationMethods All of the conversation-annotated methods
     */
    private void dispatchConversation(Module module, Map<String, Method> conversationMethods) {
        // We check if the user sending the message has an open conversation with the bot
        // if not, we create a new conversation, setting the first method.
        if (! ConversationStore.exists(this.event.user())) {
            ConversationStore.create(
                    this.event.user(),
                    this.findConversationStart(conversationMethods).getName()
            );
        }

        ConversationStore conversation = ConversationStore.find(this.event.user());

        // We find the current conversation method by getting a method from the map
        // with the key that is stored on the conversation class.
        Method currentMethod = conversationMethods.get(conversation.getNextStage());
        Dialogue annotation = currentMethod.getAnnotation(Dialogue.class);

        // We check whether the method accepts the pattern in the message
        if (this.conversationMatchesPattern(annotation.pattern())) {
            conversation.setNextStage(annotation.next());
            conversation.pushEventToHistory(currentMethod.getName(), this.event);

            this.invokeConversation(module, currentMethod, conversation);

            // Last but not least we check if the conversation has come to an end
            // in which case we remove it from the store.
            this.removeConversationIfEnded(conversation);
        }
    }

    /**
     * We find the conversation start by looping through all the methods and finding
     * the one that is not annotated as "next".
     *
     * @param conversationMethods All of the methods to be checked
     * @return The first method
     */
    private Method findConversationStart(Map<String, Method> conversationMethods) {
        for (Method method : conversationMethods.values()) {
            if (! this.methodMentionedInAnnotation(method.getName(), conversationMethods)) {
                return method;
            }
        }

        return null;
    }

    /**
     * This method decides whether the method name is mentioned in the "next" annotation
     * of the dialogue. The point being that if it's not, it is the first method of the conversation.
     *
     * @param methodName The method name to be checked
     * @param conversationMethods All of the conversation methods
     * @return Whether it is mentioned
     */
    private boolean methodMentionedInAnnotation(String methodName, Map<String, Method> conversationMethods) {
        for (Method method : conversationMethods.values()) {
            if (methodName.equals(method.getAnnotation(Dialogue.class).next())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Decides whether the event message is suitable for the conversation method,
     * based on a pattern.
     *
     * @param pattern The pattern to be checked
     * @return Whether the method accepts given pattern
     */
    private boolean conversationMatchesPattern(String pattern) {
        return this.event.text().matches(pattern);
    }

    /**
     * Checks whether the conversation has come to an end in which case
     * we remove it from the store.
     *
     * @param conversation The conversation to be removed
     */
    private void removeConversationIfEnded(ConversationStore conversation) {
        if (conversation.getNextStage().equals("")) {
            conversation.stop();
        }
    }

    /**
     * Invokes the conversation method.
     *
     * @param module The module that the method is invoked on
     * @param currentConversationStage The current conversation method
     * @param conversation The conversation that is passed to the method
     */
    private void invokeConversation(Module module, Method currentConversationStage, ConversationStore conversation) {
        try {
            currentConversationStage.invoke(
                    module,
                    this.event,
                    new Response(this.event.session().getBasicRemote()),
                    conversation
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
