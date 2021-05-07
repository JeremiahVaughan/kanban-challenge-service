package com.kanbanchallenge.kanbanchallengeservice.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanbanchallenge.kanbanchallengeservice.config.Configuration;
import com.kanbanchallenge.kanbanchallengeservice.model.Ticket;
import com.kanbanchallenge.kanbanchallengeservice.service.Cipher;
import com.kanbanchallenge.kanbanchallengeservice.utility.IdGenerator;
import net.minidev.json.JSONValue;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Repository
public class DefaultTicketRepository implements TicketRepository {

    private final Configuration configuration;
    private final Cipher cipher;

    public DefaultTicketRepository(Configuration configuration, Cipher cipher) {
        this.configuration = configuration;
        this.cipher = cipher;
        initializeFile(configuration);
    }

    @Override
    public List<Ticket> findAllTickets() {
        return new ArrayList<>(getAllTicketsAsMap().values());
    }

    /**
     * If no id is present in the ticket, then a new id will be assigned. This allows us to use a single
     * method to both save and update existing tickets.
     */
    @Override
    public List<Ticket> saveTicket(Ticket ticket) {
        Map<String, Ticket> currentTickets = getAllTicketsAsMap();
        if (ticket.getId() == null) {
            ticket.setId(IdGenerator.createId());
        }
        currentTickets.put(ticket.getId(), ticket);
        persistTickets(currentTickets);
        return findAllTickets();
    }

    /**
     * Choosing to use a map to persist the tickets rather then an array because arrays force us to iterate
     * through each value to update a particular one.
     *
     * Maps also help prevent duplication since keys are unique and the set method implicitly means update or create
     * depending on whats already there.
     */
    private boolean persistTickets(Map<String, Ticket> currentTickets) {
        boolean saveSuccessful = false;
        try {
            encryptedFileState(false);
            getObjectMapper().writeValue(getFile(), currentTickets);
            encryptedFileState(true);
            saveSuccessful = true;
        } catch (IOException e) {
            System.out.println("Unable to write to file at: " + configuration.getLoads());
            e.printStackTrace();
        }
        return saveSuccessful;
    }

    @Override
    public boolean saveAllTickets(Ticket[] tickets) {
        Map<String, Ticket> ticketMap = new HashMap<>();
        Arrays.stream(tickets).forEach(ticket -> {
            ticketMap.put(ticket.getId(), ticket);
        });
        return persistTickets(ticketMap);
    }

    /**
     * This method is responsible for creating the initial file should it not already exist.
     */
    private void initializeFile(Configuration configuration) {
        try {
            String pathLocation = configuration.getLoads();
            File persistedTickets = new File(pathLocation);
            if (persistedTickets.createNewFile()) {
                System.out.println("No existing tickets file found, " +
                        "new file created at: " + persistedTickets.getAbsolutePath());
            } else {
                System.out.println("Existing tickets file found at " +
                        persistedTickets.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Failed to create tickets file");
            e.printStackTrace();
        }
    }

    /**
     * Getting all tickets as a map is necessary for the sake of performance when performing
     * CRUD operations on the file.
     */
    private Map<String, Ticket> getAllTicketsAsMap() {
        Map<String, Ticket> result = new HashMap<>();
        try {
            if (getFile().length() != 0) {
                encryptedFileState(false);
                result = getObjectMapper().readValue(getFile(), new TypeReference<>() {
                });
                encryptedFileState(true);
            }
        } catch (IOException e) {
            System.out.println("Unable to read from file at: " + configuration.getLoads());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param encrypt sets the state of the file to encrypted or decrypted depending on the give argument
     * @throws IOException the calling methods are already catching this exception.
     *
     * Encoding/Decoding to prevent special characters from jamming up the encryption method. Encoding twice
     * before and after encrypting/decrypting to prevent loss/corruption of data due to unrecognized values.
     */
    private void encryptedFileState(boolean encrypt) throws IOException {
        String fileContent = readStringFromFile();

        if (encrypt) {
            String encodedFileContent = cipher.encode(fileContent);
            String encryptedEncodedFileContent = cipher.encrypt(encodedFileContent);
            String doubleEncodedSingleEncryptedFileContent = cipher.encode(encryptedEncodedFileContent);
            getObjectMapper().writeValue(getFile(), doubleEncodedSingleEncryptedFileContent);
        } else {
            String fileContentWithoutQuotes = fileContent.replace("\"", "");
            String decodedFileContent = cipher.decode(fileContentWithoutQuotes);
            String decryptedEncodedFileContent = cipher.decrypt(decodedFileContent);
            String decryptedDoubleDecodedFileContent = cipher.decode(decryptedEncodedFileContent);
            getObjectMapper().writeValue(getFile(), JSONValue.parse(decryptedDoubleDecodedFileContent));
        }
    }

    /**
     * We would normally read from the file using jackson since it is a json file, but for the purposes of encryption
     * its much simpler to operate on the entire file as a string rather then a node tree.
     */
    private String readStringFromFile() {
        StringBuilder stringBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(getFile().toPath(), StandardCharsets.US_ASCII)) {
            stream.forEach(stringBuilder::append);
        } catch (IOException e) {
            System.out.println("Failed to read string from file");
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    private File getFile() {
        String filePath = configuration.getLoads();
        return Paths.get(filePath).toFile();
    }
}
