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
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;

public class ThingDescription {
	
	final static String file_name = "TD.json";
	
	protected static String readTDFile(){

        
        String thingsDescription = "";
        ClassPathResource res = new ClassPathResource(file_name);    
        try {
			InputStream is = res.getInputStream();
			int content;
			while ((content = is.read()) != -1) {
				// convert to char and display it
				thingsDescription +=(char) content;
				System.out.print((char) content);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return thingsDescription;
	}

}
