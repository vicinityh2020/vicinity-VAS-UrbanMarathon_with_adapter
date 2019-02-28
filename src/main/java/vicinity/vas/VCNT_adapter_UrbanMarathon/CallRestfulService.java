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


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

public class CallRestfulService {
	public static String callService(String wsUrl, String crudVerb, ArrayList<Variable> inputs, String entity,
			 ArrayList<Variable> requestHeaderList, String contentType) {
		String result = "";
		String inputList = "";
		if (!inputs.isEmpty()) {
			for (Variable input : inputs) {
				if (input.value != null && !input.value.isEmpty()) {
					if (!inputList.isEmpty())
						inputList += "&";
					inputList += input.name + "=" + input.value;
				}
			}
		}

		// instead of using URLEncoder
		inputList = inputList.replaceAll("\\{", "%7B");
		inputList = inputList.replaceAll("\\}", "%7D");
		inputList = inputList.replaceAll("\\[", "%5B");
		inputList = inputList.replaceAll("\\]", "%5D");
		inputList = inputList.replaceAll("\\:", "%3A");

		String inputListGet = "";
		if (!inputList.isEmpty()) {
			inputListGet = "?" + inputList;
		}
		
		
		if (crudVerb.equalsIgnoreCase("get") || crudVerb.equalsIgnoreCase("post") || crudVerb.equalsIgnoreCase("delete")
				|| crudVerb.equalsIgnoreCase("put")) {
			HttpURLConnection conn = null;
			try {
				URL url = new URL((wsUrl + inputListGet).replaceAll(" ", "%20"));
				System.out.println("Calling " + url.toString());
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(30000);
				conn.setRequestMethod(crudVerb);
				conn.setRequestProperty("Accept", "application/json");
				// if (hasAuth) {
//				 Base64 b = new Base64();
//				 String encoding = b.encodeAsString(new String("user:liferay").getBytes());
//				 conn.setRequestProperty("Authorization", "Basic " + encoding);
				// }
				if (requestHeaderList != null) {
					for (Variable header : requestHeaderList) {
						conn.setRequestProperty(header.name, header.value);
					}
				}
				if (crudVerb.equalsIgnoreCase("post") || crudVerb.equalsIgnoreCase("put")) {
					if (!wsUrl.contains("gamification-portlet.gamification")){
					conn.setRequestProperty("Content-Type", contentType);
					conn.setDoInput(true);
					conn.setDoOutput(true);
					DataOutputStream os = new DataOutputStream(conn.getOutputStream());
					os.writeBytes(entity);
					} else {
						HttpClient client = HttpClientBuilder.create().build();
		                HttpPost post = new HttpPost(url.toString());
		                String USER_AGENT = "Mozilla/5.0";
		                // add header
		                post.setHeader("User-Agent", USER_AGENT);
		                //basic auth header
		                Base64 b = new Base64();
						String encoding = b.encodeAsString(new String("user:liferay").getBytes());
						post.setHeader("Authorization", "Basic " + encoding);
						post.setHeader("Content-Type", contentType);
						// urlencoded parameters
						List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
						if (wsUrl.contains("Game-calculation")){
							urlParameters.add(new BasicNameValuePair("mysecondJSON" , entity));
						}
						if (wsUrl.contains("Register-user")){
							urlParameters.add(new BasicNameValuePair("myFirstJSON" , entity));
						}
					    if (wsUrl.contains("Get-rules")){
					    	urlParameters.add(new BasicNameValuePair("mythirdJSON" , entity));
					    }
					    if (wsUrl.contains("Last-points-per-event")){
					    	urlParameters.add(new BasicNameValuePair("myJsonData" , entity));
					    }
					    if (wsUrl.contains("User-ranking")){
					    	urlParameters.add(new BasicNameValuePair("myJSONRanking" , entity));
					    }
					    HttpEntity entity2 = new UrlEncodedFormEntity(urlParameters);
					    if (entity!=null){
		                    post.setEntity(entity2);
		                }
		                HttpResponse response = client.execute(post);
		                System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		                StringBuffer resultPost = new StringBuffer();
		                String line = "";
		                while ((line = rd.readLine()) != null) {
		                    resultPost.append(line);
		                }
		                result = resultPost.toString();
		                return result;
					}

				}
				if (conn.getResponseCode() != 200 && conn.getResponseCode() != 201 && conn.getResponseCode() != 301) {
					throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
				}
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				String output;
				System.out.println("Output from Server ....\n");
				while ((output = br.readLine()) != null) {
					result += output;
					System.out.println(output);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				result = "{\"error_message\": \"" + e.toString()  + "\"}";
			} catch (IOException e) {
				e.printStackTrace();
				result = "{\"error_message\": \"" + e.toString()  + "\"}";
			} finally {
				if (conn != null) {
					conn.disconnect();
				}
			}
		} else {
			System.out.println("Operation " + crudVerb + " is not a valid CRUD operation");
		}
		if (result.startsWith("[")) {
			result = "{\"Property\":" + result + "}";
		}
		return result;
	}
	
	public static class Variable {
		public String name;
		public String value;
		public String type;
		public ArrayList<Variable> subtypes = new ArrayList<Variable>();
		public ArrayList<Variable> arrayElements = new ArrayList<Variable>();

		Variable(String name, String value, String type) {
			this.name = name;
			this.value = value;
			this.type = type;
		}

		Variable(Variable prototype) {
			this.name = prototype.name;
			this.value = prototype.value;
			this.type = prototype.type;
			for (Variable sub : prototype.subtypes) {
				Variable arg = new Variable(sub);
				subtypes.add(arg);
			}
			for (Variable el : prototype.arrayElements) {
				Variable arg = new Variable(el);
				arrayElements.add(arg);
			}
		}

		public Variable getSubtype(String name) {
			for (Variable sub : subtypes) {
				if (sub.name.equals(name.replaceAll("[^A-Za-z]", ""))) {
					return sub;
				}
			}
			return null;
		}
	}
}
