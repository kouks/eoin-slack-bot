package io.pavelkoch.eoin.exceptions;

public class Handler {
    /**
     * This method takes an exception as an argument and renders it to the console.
     *
     * @param e The exception to be rendered
     */
    public static void render(Exception e) {
        System.out.println("Something went horribly wrong :( ");
        System.out.println(e.toString());

        System.exit(1);
    }
}
