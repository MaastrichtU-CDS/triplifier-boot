package nl.um.cds.triplifierboot.entity;

import javax.persistence.*;

@Entity
public class TaskEntity {

    public enum Status {
        QUEUE,
        RUNNING,
        COMPLETE,
        ERROR
    }

    @Enumerated(EnumType.STRING)
    private Status status = Status.QUEUE;

    private @Id @GeneratedValue
    Long id;

    private String ontologyAndOrData;

    private String propertiesFilePath;

    private String ontologyFilePath;

    private String outputFilePath;

    boolean ontologyParsing = true;
    boolean dataParsing = true;
    boolean clearDataGraph = false;

    private String baseUri;

    boolean repeatTask = false;



}
