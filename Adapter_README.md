# 1. Infrastructure overview
Vicinity Adapter for Urban Marathon Value Added Service (3.2.2) of MPH Pilot Case.

# 2. Configuration and deployment
## Build using Maven

In the root folder of the project:

`mvn clean install`

## Run
The adapter is the interface of this VAS to Vicinity. In order to run this VAS, in the target folder of the project:

`java -jar adapter-0.0.1-SNAPSHOT.jar`

# 3. Functionality and API

## Endpoints
* GET /objects (optional): Retrieve all devices Thing Descriptions(TDs) for registration to VICINITY. This functionality is optional since auto-registration will be performed, so the adapter will send the TDs to the agent at start-up.
* PUT /objects/{oid}/events/{eid}: This endpoint will be called each time the topic has changed in order to inform the VAS for new measurements

## Functions
*	Send device TDs to the vicinity agent at start up
