package nl.um.cds.triplifierboot.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class TaskEntity {

    public enum Status {
        QUEUE,
        RUNNING,
        COMPLETE,
        ERROR
    }

    private Date dateCreated = new Date();

    @Enumerated(EnumType.STRING)
    private Status status = Status.QUEUE;

    private String errorMessage = "";

    private @Id
    UUID id = UUID.randomUUID();

    private String ontologyAndOrData;

    private String propertiesFilePath;

    private String ontologyFilePath;

    private String outputFilePath;

    private Boolean ontologyParsing = null;
    private Boolean dataParsing = null;
    private Boolean clearDataGraph = null;

    private String baseUri = null;

    boolean repeatTask = false;


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOntologyAndOrData() {
        return ontologyAndOrData;
    }

    public void setOntologyAndOrData(String ontologyAndOrData) {
        this.ontologyAndOrData = ontologyAndOrData;
    }

    public String getPropertiesFilePath() {
        return propertiesFilePath;
    }

    public void setPropertiesFilePath(String propertiesFilePath) {
        this.propertiesFilePath = propertiesFilePath;
    }

    public String getOntologyFilePath() {
        return ontologyFilePath;
    }

    public void setOntologyFilePath(String ontologyFilePath) {
        this.ontologyFilePath = ontologyFilePath;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    public Boolean getOntologyParsing() {
        return ontologyParsing;
    }

    public void setOntologyParsing(Boolean ontologyParsing) {
        this.ontologyParsing = ontologyParsing;
    }

    public Boolean getDataParsing() {
        return dataParsing;
    }

    public void setDataParsing(Boolean dataParsing) {
        this.dataParsing = dataParsing;
    }

    public Boolean getClearDataGraph() {
        return clearDataGraph;
    }

    public void setClearDataGraph(Boolean clearDataGraph) {
        this.clearDataGraph = clearDataGraph;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public boolean isRepeatTask() {
        return repeatTask;
    }

    public void setRepeatTask(boolean repeatTask) {
        this.repeatTask = repeatTask;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return "TaskEntity{" +
                "status=" + status +
                ", errorMessage='" + errorMessage + '\'' +
                ", id=" + id +
                ", ontologyAndOrData='" + ontologyAndOrData + '\'' +
                ", propertiesFilePath='" + propertiesFilePath + '\'' +
                ", ontologyFilePath='" + ontologyFilePath + '\'' +
                ", outputFilePath='" + outputFilePath + '\'' +
                ", ontologyParsing=" + ontologyParsing +
                ", dataParsing=" + dataParsing +
                ", clearDataGraph=" + clearDataGraph +
                ", baseUri='" + baseUri + '\'' +
                ", repeatTask=" + repeatTask +
                '}';
    }
}
