package nl.um.cds.triplifierboot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "task")
// overwrite defaults using spring boots conventions (e.g. environment vars or yaml properties)
public class TaskProperties {

    private Long pollMs;

    private String propertiesFile = "triplifier.properties";

    private String workdir = "triplifier-workdir";
    private String outputFile = "output.ttl";
    private String ontologyFile = "output.owl";

    private String sparqlType = "rdf4j";
    private String sparqlUrl = "http://localhost:7200";
    private String sparqlDb = "epnd_dummy";

    public String getSparqlType() {
        return sparqlType;
    }

    public void setSparqlType(String sparqlType) {
        this.sparqlType = sparqlType;
    }

    public String getSparqlUrl() {
        return sparqlUrl;
    }

    public void setSparqlUrl(String sparqlUrl) {
        this.sparqlUrl = sparqlUrl;
    }

    public String getSparqlDb() {
        return sparqlDb;
    }

    public void setSparqlDb(String sparqlDb) {
        this.sparqlDb = sparqlDb;
    }

    boolean ontologyParsing = false;
    boolean dataParsing = true;
    boolean clearDataGraph = false;

    public Long getPollMs() {
        return pollMs;
    }

    public void setPollMs(Long pollMs) {
        this.pollMs = pollMs;
    }

    public String getPropertiesFile() {
        return propertiesFile;
    }

    public void setPropertiesFile(String propertiesFile) {
        this.propertiesFile = propertiesFile;
    }

    public String getWorkdir() {
        return workdir;
    }

    public void setWorkdir(String workdir) {
        this.workdir = workdir;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getOntologyFile() {
        return ontologyFile;
    }

    public void setOntologyFile(String ontologyFile) {
        this.ontologyFile = ontologyFile;
    }

    public boolean isOntologyParsing() {
        return ontologyParsing;
    }

    public void setOntologyParsing(boolean ontologyParsing) {
        this.ontologyParsing = ontologyParsing;
    }

    public boolean isDataParsing() {
        return dataParsing;
    }

    public void setDataParsing(boolean dataParsing) {
        this.dataParsing = dataParsing;
    }

    public boolean isClearDataGraph() {
        return clearDataGraph;
    }

    public void setClearDataGraph(boolean clearDataGraph) {
        this.clearDataGraph = clearDataGraph;
    }
}
