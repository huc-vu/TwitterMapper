package query;

import filters.Filter;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Layer;
import twitter4j.Status;
import ui.MapMarkerImage;
import ui.MapMarkerSimple;
import ui.QueryPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * A query over the twitter stream.
 */
public class Query implements Observer{
    // The map on which to display markers when the query matches
    private final JMapViewer map;
    // Each query has its own "layer" so they can be turned on and off all at once
    private Layer layer;
    // The color of the outside area of the marker
    private final Color color;
    // The string representing the filter for this query
    private final String queryString;
    // The filter parsed from the queryString
    private final Filter filter;
    // The checkBox in the UI corresponding to this query (so we can turn it on and off and delete it)
    private JCheckBox checkBox;

    private QueryPanel panel;


    public Color getColor() {
        return color;
    }
    public String getQueryString() {
        return queryString;
    }
    public Filter getFilter() {
        return filter;
    }
    public Layer getLayer() {
        return layer;
    }
    public JCheckBox getCheckBox() {
        return checkBox;
    }
    public void setCheckBox(JCheckBox checkBox) {
        this.checkBox = checkBox;
    }
    public void setVisible(boolean visible) {
        layer.setVisible(visible);
    }
    public boolean getVisible() { return layer.isVisible(); }

    public Query(String queryString, Color color, JMapViewer map) {
        this.queryString = queryString;
        this.filter = Filter.parse(queryString);
        this.color = color;
        this.layer = new Layer(queryString);
        this.map = map;
        this.panel = new QueryPanel();
    }

    @Override
    public String toString() {
        return "Query: " + queryString;
    }

    /**
     * Method called everytime this class is notified of the arrival of a new tweet
     * @param twitter TwitterSource that extends Observable
     * @param status  New tweet
     */
    @Override
    public void update(Observable twitter, Object status) {
        Status s = (Status)status;
        if(filter.matches(s)){
            // tweet's location
            Coordinate coord = util.Util.statusCoordinate(s);
            //System.out.println(s.getPlace().getURL());

            // add a marker at this location
            //map.addMapMarker(new MapMarkerColored(layer, coord, color));
            map.addMapMarker(new MapMarkerImage(layer, coord, color, s.getUser().getProfileImageURL(), s));

            panel.incrementTweets();
        }
    }

    /**
     * This query is no longer interesting, so terminate it and remove all traces of its existence.
     */
    public void terminate() {
        layer.setVisible(false);
    }

    public QueryPanel getPanel() { return panel; }
}

