package net.straininfo2.grs.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.WebResource;

/**
 * Service that provides external access to megx data for genomes.
 * 
 * @author wdesmet
 * 
 */
public class MegxGenomeService {

	private WebResource megx; // WebResource pointing to megx endpoint
	
	private final static Logger logger = LoggerFactory.getLogger(MegxGenomeService.class);

	public MegxGenomeService() {

	}

	public MegxGenomeService(WebResource megx) {
		this.megx = megx;
	}

	public void setMegx(WebResource megx) {
		this.megx = megx;
	}

	public WebResource getMegx() {
		return megx;
	}

	public String getMegxPlain(long id) {
		return megx.queryParam("id", "gpid:" + id).accept("text/plain").get(
				String.class);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> getMegxJson(long id) throws JSONException {
		JSONObject obj = megx.queryParam("id", "gpid:" + id).accept(
				MediaType.APPLICATION_JSON_TYPE).get(
						JSONObject.class).getJSONObject("genome");
		Iterator<String> iter = (Iterator<String>)obj.keys();
		Map<String, String> res = new HashMap<String, String>();
		while (iter.hasNext()) {
			String key = iter.next();
			if (obj.get(key) != JSONObject.NULL) {
				res.put(key, obj.getString(key));
			}
			else {
				logger.trace("Discarded NULL object for key {}", key);
			}
		}
		return res;
	}
}
