/**
Copyright 2018-2019. Information Technologies Institute (CERTH-ITI)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package vicinity.vas.VCNT_adapter_UrbanMarathon;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import vicinity.vas.VCNT_adapter_UrbanMarathon.CallRestfulService.Variable;
import vicinity.vas.VCNT_adapter_UrbanMarathon.Responses.Response;

@Controller
public class VcntAdapterUrbarMarathonController {

	final String BASE_URI = "http://vicinity.urbanmarathon.iti.gr:8099/api/jsonws/gamification-portlet.gamification";

	///////////////////////////////////// event
	///////////////////////////////////// ////////////////////////////////////////////////////////////////
	/**
	 * @param pid:
	 *            pid can be one of: user_registration push_measurement
	 *            get_rules user_ranking get_points_for_user
	 * @param oid:
	 *            the urban marathon service id in the infrastructure
	 * @param request:
	 *            it is different for each call
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/objects/{oid}/properties/{pid}", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<?> generateResponseEvent(@PathVariable("pid") String pid, @PathVariable("oid") String oid,
			@RequestBody String request) throws Exception {
		//
		String result = "";
		String entity = request.toString();
		if (pid.equals("Register-user")) {

			String wsUrl = BASE_URI + "/Register-user";
			ArrayList<Variable> inputs = new ArrayList<Variable>();
			ArrayList<Variable> requestHeaderList = new ArrayList<Variable>();

			result = CallRestfulService.callService(wsUrl, "POST", inputs, entity, requestHeaderList,
					"application/x-www-form-urlencoded");
			
		} else if (pid.equals("push_measurement")) {

			String wsUrl = BASE_URI + "/Game-calculation";
			ArrayList<Variable> inputs = new ArrayList<Variable>();
			ArrayList<Variable> requestHeaderList = new ArrayList<Variable>();

			result = CallRestfulService.callService(wsUrl, "POST", inputs, entity, requestHeaderList,
					"application/x-www-form-urlencoded");

		} else if (pid.equals("Get-rules")) {
			
			String wsUrl = BASE_URI + "/Get-rules";
			ArrayList<Variable> inputs = new ArrayList<Variable>();
			ArrayList<Variable> requestHeaderList = new ArrayList<Variable>();

			result = CallRestfulService.callService(wsUrl, "POST", inputs, entity, requestHeaderList,
					"application/x-www-form-urlencoded");

		} else if (pid.equals("user_ranking")) {
			String wsUrl = BASE_URI + "/User-ranking";
			ArrayList<Variable> inputs = new ArrayList<Variable>();
			ArrayList<Variable> requestHeaderList = new ArrayList<Variable>();

			result = CallRestfulService.callService(wsUrl, "POST", inputs, entity, requestHeaderList,
					"application/x-www-form-urlencoded");

		} else if (pid.equals("get_points_for_user")) {
			String wsUrl = BASE_URI + "/Last-points-per-event";
			ArrayList<Variable> inputs = new ArrayList<Variable>();
			ArrayList<Variable> requestHeaderList = new ArrayList<Variable>();

			result = CallRestfulService.callService(wsUrl, "POST", inputs, entity, requestHeaderList,
					"application/x-www-form-urlencoded");
		}

		System.out.println(result);
		// Return the response
		if (result.contains("\"error_message\"")) {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	////////////////////////////////////////// Thing Descriptions
	////////////////////////////////////////// ///////////////////////////////////////////////
	@RequestMapping(value = "/objects", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String generateResponse() throws Exception {
		String response = "";
		response = ThingDescription.readTDFile();
		return response;
	}

}
