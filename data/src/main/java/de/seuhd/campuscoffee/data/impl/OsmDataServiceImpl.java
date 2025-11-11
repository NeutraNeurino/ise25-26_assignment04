package de.seuhd.campuscoffee.data.impl;

import de.seuhd.campuscoffee.domain.exceptions.OsmNodeNotFoundException;
import de.seuhd.campuscoffee.domain.model.OsmNode;
import de.seuhd.campuscoffee.domain.ports.OsmDataService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Service
public class OsmDataServiceImpl implements OsmDataService {

    private static final String OSM_NODE_URL = "https://www.openstreetmap.org/api/0.6/node/{id}";

    private final RestTemplate restTemplate;

    public OsmDataServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public OsmNode loadNode(long osmNodeId) {
        try {
            String xml = restTemplate.getForObject(OSM_NODE_URL, String.class, osmNodeId);
            if (xml == null || xml.isBlank()) {
                throw new OsmNodeNotFoundException(osmNodeId);
            }
            return parseOsmNode(xml, osmNodeId);
        } catch (HttpClientErrorException.NotFound e) {
            throw new OsmNodeNotFoundException(osmNodeId);
        } catch (RestClientException e) {
            // Du kannst hier eine eigene Exception einführen, wenn du magst.
            throw e;
        }
    }

    private OsmNode parseOsmNode(String xml, long osmNodeId) {
    try {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xml)));

        NodeList nodeList = document.getElementsByTagName("node");
        if (nodeList.getLength() == 0) {
            throw new OsmNodeNotFoundException(osmNodeId);
        }

        Element nodeElement = (Element) nodeList.item(0);
        long id = Long.parseLong(nodeElement.getAttribute("id"));
        double lat = Double.parseDouble(nodeElement.getAttribute("lat"));
        double lon = Double.parseDouble(nodeElement.getAttribute("lon"));

        Map<String, String> tags = new HashMap<>();
        NodeList tagNodes = nodeElement.getElementsByTagName("tag");
        for (int i = 0; i < tagNodes.getLength(); i++) {
            Element tag = (Element) tagNodes.item(i);
            String key = tag.getAttribute("k");
            String value = tag.getAttribute("v");
            tags.put(key, value);
        }

        return OsmNode.builder()
                .nodeId(id)        // passt zu record field 'nodeId'
                .latitude(lat)     // passt zu 'latitude'
                .longitude(lon)    // passt zu 'longitude'
                .tags(tags)        // passt zu 'tags'
                .build();

    } catch (Exception e) {
        throw new RuntimeException("Failed to parse OSM XML for node " + osmNodeId, e);
    }
}
}
