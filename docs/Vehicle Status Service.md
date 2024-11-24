# Vehicle Status Service

Practical task

## Functionality

The task is to develop REST API Service that provides vehicle status reports. The report is generated based on the data provided by two third party data providers. You can find the service definitions further down.

### Technology Stack

The solution must be implemented using the following:

1. Java
2. Micronaut - https://micronaut.io/
3. Micronaut async support via https://projectreactor.io Flux/Mono primitives - the entire solution must be non-blocking

Please note that the third party services described below as fictitious. As part of the task you need to create mocks for these APIs and use them to implement integration tests.

### Functional Requirements

1. If the **accident_free** feature is enabled then the *accident_free* field in the response is populated based on the return from the ***Insurance*** data provider. If there are any insurance claims for the vehicle it is not considered accident free anymore.

2. If the **maintenance** feature is enabled then the ***Maintenance*** data provider is called and the *maintenance_score* field  is determined based on the maintenance frequency as follows:
	- very_low, low -> poor
	- medium -> average
	- high -> good

3. All input parameters must be **validated**.

4. **Audit** logs. For each request to the service an unique *request_id* is generated. Each log message must contain it. Log message must be generated when:
	-  Request is received
	-  Call to 3rd party is made
	-  Request is processed or failed



### REST API service definition

POST http://car-checker.com/check

#### Request Body

    {
	    "vin": "4Y1SL65848Z411439", // The unique identification number of the vehicle (VIN)
	    "features": ["accident_free", "maintenance"] // Possible values: "accident_free", "maintenance". At least one feature must be provided
    }


#### Response Body Example

    {
	    "request_id": "xxxx",
	    "vin": "4Y1SL65848Z411439",
	    "accident_free": false,
	    "maintenance_score": "average" // Possible values: poor, average, good
    }


## 3rd Party Data providers
### Insurance

GET https://insurance.com/accidents/report?vin={vin_number}

#### 200 Response Example:

    {
	    "report": {
		    "claims": 3 // Number of insurance claims for the vehicle
	    }
    }

#### 404 - VIN number not found


### Maintenance

GET https://topgarage.com/cars/{vin}

#### 200 Response Example:
    {
	    "maintenance_frequency": "low" // Possible values: very-low, low, medium, high
    }


## What we are looking for

1. All functionality implemented and working correctly

2. All functionality is covered by tests

3. Proper architecture of the codebase
	- Decoupled components providing behaviors
	- Components composed to aggregate behaviors
	- Clean, maintainable code with appropriate comments

4. Proper error handling

	- Surfacing errors to the user in a useful way
	- Automatic retries on the backend

5. Optimized code


Thank you, and please reach out if you have any questions!
