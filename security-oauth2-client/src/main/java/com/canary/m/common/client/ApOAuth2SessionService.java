package com.canary.m.common.client;


import com.canary.core.acl.AclAccountDTO;
import lombok.extern.slf4j.Slf4j;
import org.jaf.context.http.client.ApRestClient;
import org.jaf.context.http.client.RestClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

/**
 * Created by hsk on 2017/1/17.
 */
@Slf4j
@Component
public class ApOAuth2SessionService {

    @Autowired
    private ApRestClient apRestClient;

    public boolean reloadSessionByUserAccount(String username) {
        try {
            String password = findAccountPassword(username);
            String url = "http://localhost:12001/v1" + "/oauth/token?password=" + password + "&username=" + username + "&grant_type=password";
//            String url = ServiceDiscovery.getServicePath(ServiceRange.DISPLAY_OAUTH) + "/oauth/token?password=" + password + "&username=" + username + "&grant_type=password";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Basic Y2xpZW50YXBwOjEyMzQ1Ng==");
            // ResponseEntity<String> result =
            apRestClient.getRestTemplate().exchange(url, HttpMethod.POST, new HttpEntity<>("parameters", headers), String.class);
            // System.out.println(result.getBody());
            return true;
        } catch (RestClientException e) {
            log.error("reloadSessionByUserAccount error", e);
            return false;
        }
    }

    public String findAccountPassword(String username) throws RestClientException {
        String url = "http://localhost:11001/v1" + "/acl/account/account?account=" + username;
                // String url = ServiceDiscovery.getServicePath(ServiceRange.DISPLAY_USER) + "/acl/account/account?account=" + username;
        AclAccountDTO aclAccountDTO = apRestClient.getForObject(url, AclAccountDTO.class);
        if (aclAccountDTO == null) {
            return null;
        }
        return aclAccountDTO.getPassword();
    }


}
