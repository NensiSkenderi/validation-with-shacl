package org.example;

import org.eclipse.rdf4j.exceptions.ValidationException;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.vocabulary.RDF4J;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.shacl.ShaclSail;

import java.io.IOException;
import java.io.StringReader;

public class ShaclValidation {
    public static void main(String[] args) {
        ShaclSail shaclSail = new ShaclSail(new MemoryStore());
        SailRepository repository = new SailRepository(shaclSail);

        repository.init();

        try {
            RepositoryConnection repositoryConnection = repository.getConnection();
            repositoryConnection.begin();

            StringReader shaclRules = new StringReader(
                    String.join("\n", "",
                            "@prefix ex: <http://example.com/ns#> .",
                            "@prefix sh: <http://www.w3.org/ns/shacl#> .",
                            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .",
                            "@prefix nensi: <http://xmlns.com/foaf/0.1/>.",

                            "ex:PersonShape",
                            "   a sh:NodeShape ;",
                            "   sh:targetClass nensi:Person ;",
                            "   sh:property ex:PersonShapeProperty .",


                            "ex:PersonShapeProperty",
                            "   sh:path nensi:age ;",
                            "   sh:datatype xsd:int ;",
                            "   sh:maxCount 1 ;",
                            "   sh:minCount 1 ."
                    )
            );

            repositoryConnection.add(shaclRules, "", RDFFormat.TURTLE, RDF4J.SHACL_SHAPE_GRAPH);
            repositoryConnection.commit();

            repositoryConnection.begin();

            StringReader invalidSampleData_DatatypeConstraintComponent = new StringReader(
                    String.join("\n", "",
                            "@prefix ex: <http://example.com/ns#> .",
                            "@prefix sh: <http://www.w3.org/ns/shacl#> .",
                            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .",
                            "@prefix nensi: <http://xmlns.com/foaf/0.1/>.",

                            "ex:testing a nensi:Person ;",
                            "   nensi:age 20, \"30\"^^xsd:int ."
                    )
            );

            StringReader invalidSampleData_MaxCountConstraintComponent = new StringReader(
                    String.join("\n", "",
                            "@prefix ex: <http://example.com/ns#> .",
                            "@prefix sh: <http://www.w3.org/ns/shacl#> .",
                            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .",
                            "@prefix nensi: <http://xmlns.com/foaf/0.1/>.",

                            "ex:testing a nensi:Person ;",
                            "   nensi:age \"28\"^^xsd:int ;",
                            "   nensi:age \"20\"^^xsd:int ."
                    )
            );

            repositoryConnection.add(invalidSampleData_DatatypeConstraintComponent, RDFFormat.TURTLE);
            repositoryConnection.add(invalidSampleData_MaxCountConstraintComponent, RDFFormat.TURTLE);
            repositoryConnection.commit();
        } catch (RepositoryException exception) {
            System.out.println("testing failed validation");
            Throwable cause = exception.getCause();
            if (cause instanceof ValidationException) {
                Model validationReportModel = ((ValidationException) cause).validationReportAsModel();
                Rio.write(validationReportModel, System.out, RDFFormat.TURTLE);
            }

            exception.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}