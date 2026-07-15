package com.tuservidor.custommessages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.slf4j.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class MessagesConfig {

    private final Path dataDirectory;
    private final Logger logger;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private Map<String, Object> raw;

    // Claves = todos los mensajes internos de Velocity que interceptamos y hacemos editables
    private static final Map<String, String> DEFAULTS = new LinkedHashMap<>();
    static {
        DEFAULTS.put("kicked-while-connected",
            "<red>Has sido echado de <yellow><server></yellow>:</red> <reason>");
        DEFAULTS.put("kicked-while-connecting",
            "<reason>");
        DEFAULTS.put("already-connected-proxy",
            "<red>Ya estas conectado a este proxy.</red>");
        DEFAULTS.put("already-connected-server",
            "<red>Ya estas conectado a este servidor.</red>");
        DEFAULTS.put("connection-in-progress",
            "<red>Ya te estas conectando a un servidor.</red>");
        DEFAULTS.put("internal-connection-error",
            "<red>Error interno de conexion. Contacta a un administrador.</red>");
        DEFAULTS.put("unexpected-disconnect",
            "<red>Desconexion inesperada del servidor. Puede que se haya caido.</red>");
        DEFAULTS.put("moved-after-kick",
            "<red>Fuiste movido de servidor porque te expulsaron de <yellow><server></yellow>.</red>");
        DEFAULTS.put("no-available-servers",
            "<red>No hay servidores disponibles a los que conectarte en este momento.</red>");
        DEFAULTS.put("server-shutdown-generic",
            "<red>El servidor <yellow><server></yellow> se ha cerrado.</red>");
        // NUEVO: mensaje de respaldo configurable cuando el backend no envia ninguna razon de kick.
        // Antes este texto estaba escrito directamente en el codigo Java (hardcodeado y con una
        // falta de ortografia: "Sin razon especifica"). Ahora es 100% editable desde este archivo.
        DEFAULTS.put("default-kick-reason",
            "Sin razón especificada");
    }

    public MessagesConfig(Path dataDirectory, Logger logger) {
        this.dataDirectory = dataDirectory;
        this.logger = logger;
    }

    public void loadOrCreate() {
        try {
            if (!Files.exists(dataDirectory)) {
                Files.createDirectories(dataDirectory);
            }
            File file = dataDirectory.resolve("messages.yml").toFile();

            if (!file.exists()) {
                writeDefaults(file);
            }

            Yaml yaml = new Yaml();
            try (InputStream in = new FileInputStream(file)) {
                raw = yaml.load(in);
            }
            if (raw == null) raw = new LinkedHashMap<>();

            boolean changed = false;
            for (Map.Entry<String, String> entry : DEFAULTS.entrySet()) {
                if (!raw.containsKey(entry.getKey())) {
                    raw.put(entry.getKey(), entry.getValue());
                    changed = true;
                }
            }
            if (changed) {
                saveToFile(file);
            }

        } catch (IOException e) {
            logger.error("No se pudo cargar messages.yml", e);
            raw = new LinkedHashMap<>(DEFAULTS);
        }
    }

    private void writeDefaults(File file) throws IOException {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        try (Writer writer = new FileWriter(file)) {
            writer.write("# VelocityCustomMessages\n");
            writer.write("# Placeholders disponibles: <server>, <reason>\n");
            writer.write("# Formato de texto: MiniMessage -> https://docs.advntr.dev/minimessage/format.html\n");
            writer.write("# 'default-kick-reason' se usa cuando el backend no envia ninguna razon de kick.\n");
            writer.write("# Despues de editar, usa: /customessages reload\n\n");
            yaml.dump(DEFAULTS, writer);
        }
    }

    private void saveToFile(File file) throws IOException {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        try (Writer writer = new FileWriter(file)) {
            yaml.dump(raw, writer);
        }
    }

    /**
     * Devuelve el texto configurable de "default-kick-reason" ya convertido a Component.
     * Se usa como fallback cuando el evento de Velocity no trae ninguna razon de kick.
     */
    public Component getDefaultKickReason() {
        String template = String.valueOf(raw.getOrDefault("default-kick-reason", DEFAULTS.get("default-kick-reason")));
        return miniMessage.deserialize(template);
    }

    public Component get(String key, Component reasonComponent, String serverName) {
        String template = String.valueOf(raw.getOrDefault(key, DEFAULTS.get(key)));

        String reasonSerialized = reasonComponent == null
            ? miniMessage.serialize(getDefaultKickReason())
            : miniMessage.serialize(reasonComponent);

        String filled = template
            .replace("<server>", serverName == null ? "" : serverName)
            .replace("<reason>", reasonSerialized);

        return miniMessage.deserialize(filled);
    }

    public Component getPlain(String key, String serverName) {
        return get(key, Component.empty(), serverName);
    }

    public void reload() {
        loadOrCreate();
    }
}
