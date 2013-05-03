package net.straininfo2.grs.dao;

import net.straininfo2.grs.dto.ComparisonData;
import net.straininfo2.grs.resource.ComparisonResource;
import org.codehaus.jettison.json.JSONException;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wdesmet
 * Date: 5/3/13
 * Time: 10:49 AM
 * To change this template use File | Settings | File Templates.
 */
public interface MashupService {
    List<Object> getCrossrefData(long genomeId, ComparisonResource comparisonResource) throws JSONException;

    ComparisonData getComparisonData(long genomeId, ComparisonResource comparisonResource) throws JSONException;
}
