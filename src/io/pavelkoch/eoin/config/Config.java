package io.pavelkoch.eoin.config;

import io.pavelkoch.eoin.Eoin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final String CONFIG_FILE = "config.properties";
    private Properties config;

    public Config load() throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(new File("config.properties")));
        this.config = prop;

        return this;
    }

    public String get(String prop) {
        return this.config.getProperty(prop);
    }
}
