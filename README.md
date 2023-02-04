# validation-with-shacl

## About The Project

In this project I have demonstrated the use of Shacl Shapes Language (a language used to validate RDF 
Triples graphs against a set of conditions) with a simple code.

**Shacles Rules**

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


**The above rules mean that:**

 1. nensi:age property should be of type int
 2. the triple should have only 1 nensi:age property

Please check this link -> https://rdf4j.org/documentation/programming/shacl/ for more info

## Contact 
Nensi Skenderi - nensiskenderi20@gmail.com
