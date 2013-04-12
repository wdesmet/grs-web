package net.straininfo2.grs.dto;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ComparisonData {

    private ConcurrentHashMap<String, String> megxData;

    private ConcurrentHashMap<String, String> strainInfoData;

    private ConcurrentHashMap<String, String> ncbiData;

    private final static String EMPTY_VALUE = "null";

    public ComparisonData() {
        this.megxData = new ConcurrentHashMap<String, String>();
        this.strainInfoData = new ConcurrentHashMap<String, String>();
        this.ncbiData = new ConcurrentHashMap<String, String>();
    }

    public void addMegxKeyValue(String key, String value) {
        this.megxData.putIfAbsent(key, value);
    }

    public void addStrainInfoKeyValue(String key, String value) {
        this.strainInfoData.putIfAbsent(key, value);
    }

    public void addNcbiKeyValue(String key, String value) {
        this.ncbiData.putIfAbsent(key, value);
    }

    public Set<String> collectMegxKeys() {
        return new HashSet<String>(megxData.keySet());
    }

    public Set<String> collectStrainInfoKeys() {
        return new HashSet<String>(strainInfoData.keySet());
    }

    public Set<String> collectNcbiKeys() {
        return new HashSet<String>(ncbiData.keySet());
    }

    public Collection<String> collectAllKeys() {
        Set<String> keys = collectMegxKeys();
        keys.addAll(collectStrainInfoKeys());
        keys.addAll(collectNcbiKeys());
        return keys;
    }

    public Map<String, String> getMegx() {
        return new HashMap<String, String>(megxData);
    }

    public void setMegx(Map<String, String> megxData) {
        this.megxData = convertMap(megxData);
    }

    public Map<String, String> getStrainInfo() {
        return new HashMap<String, String>(strainInfoData);
    }

    public void setStrainInfo(Map<String, String> strainInfoData) {
        this.strainInfoData = convertMap(strainInfoData);
    }

    public Map<String, String> getNcbi() {
        return new HashMap<String, String>(ncbiData);
    }

    public void setNcbi(Map<String, String> ncbiData) {
        this.ncbiData = convertMap(ncbiData);
    }

    /*
    input hashmaps can contain nulls, concurrent maps can't
     */
    private ConcurrentHashMap<String, String> convertMap(Map<String, String> map) {
        ConcurrentHashMap<String, String> result = new ConcurrentHashMap<String, String>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue() != null) {
                result.put(entry.getKey(), entry.getValue());
            }
            else {
                result.put(entry.getKey(), EMPTY_VALUE);
            }
        }
        return result;
    }
}
