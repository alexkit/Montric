package org.eurekaJ.manager.json;

import java.util.ArrayList;
import java.util.List;

import org.eurekaj.manager.berkeley.treemenu.TreeMenuNode;
import org.eurekaj.manager.json.BuildJsonObjectsUtil;
import org.jsflot.xydata.XYDataList;
import org.jsflot.xydata.XYDataPoint;
import org.jsflot.xydata.XYDataSetCollection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class BuildJsonObjectsUtilTest {

    @Test
    public void test_that_an_empty_array_creates_empty_json_object() throws JSONException {
        List<TreeMenuNode> emptyList = new ArrayList<TreeMenuNode>();

        JSONObject jsonObject = BuildJsonObjectsUtil.buildTreeTypeMenuJsonObject("treeMenuID", emptyList, 0, 20, false);
        String actual = jsonObject.toString();
        assertEquals("{\"treeMenuID\":[]}", actual);
    }

    @Test
    public void test_that_one_node_element_returns_correct_json_object() throws JSONException {
        List<TreeMenuNode> nodeList = new ArrayList<TreeMenuNode>();
        nodeList.add(new TreeMenuNode("A", "Y"));

        JSONObject jsonObject = BuildJsonObjectsUtil.buildTreeTypeMenuJsonObject("treeMenuID", nodeList, 0, 20, false);
        assertEquals("{\"treeMenuID\":[{\"chartGrid\":\"A\",\"guid\":\"A\",\"hasChildren\":false,\"childrenNodes\":[],\"isSelected\":false,\"name\":\"A\",\"treeItemIsExpanded\":false,\"guiPath\":\"A\",\"availableCharts\":[],\"parentPath\":null}]}", jsonObject.toString());
    }

    @Test
    public void test_that_two_node_element_returns_correct_json_object() throws JSONException {
        List<TreeMenuNode> nodeList = new ArrayList<TreeMenuNode>();
        nodeList.add(new TreeMenuNode("A", "Y"));
        nodeList.add(new TreeMenuNode("B", "Y"));

        StringBuilder expected = new StringBuilder();
        expected.append("{\"treeMenuID\":[");
        expected.append("{\"chartGrid\":\"A\",\"guid\":\"A\",\"hasChildren\":false,\"childrenNodes\":[],\"isSelected\":false,\"name\":\"A\",\"treeItemIsExpanded\":false,\"guiPath\":\"A\",\"availableCharts\":[],\"parentPath\":null}");
        expected.append(",");
        expected.append("{\"chartGrid\":\"B\",\"guid\":\"B\",\"hasChildren\":false,\"childrenNodes\":[],\"isSelected\":false,\"name\":\"B\",\"treeItemIsExpanded\":false,\"guiPath\":\"B\",\"availableCharts\":[],\"parentPath\":null}");
        expected.append("]}");

        JSONObject jsonObject = BuildJsonObjectsUtil.buildTreeTypeMenuJsonObject("treeMenuID", nodeList, 0, 20, false);
        assertEquals(expected.toString(), jsonObject.toString());
    }

    @Test
    public void test_that_second_level_nodes_generated_correct_json() throws JSONException {
        List<TreeMenuNode> nodeList = new ArrayList<TreeMenuNode>();
        nodeList.add(new TreeMenuNode("A", "Y"));
        nodeList.add(new TreeMenuNode("B", "Y"));
        nodeList.add(new TreeMenuNode("A:C", "Y"));

        StringBuilder expected = new StringBuilder();
        expected.append("{\"treeMenuID\":[");
        expected.append("{\"chartGrid\":\"A\",\"guid\":\"A\",\"hasChildren\":false,\"childrenNodes\":[],\"isSelected\":false,\"name\":\"A\",\"treeItemIsExpanded\":false,\"guiPath\":\"A\",\"availableCharts\":[\"A:C\"],\"parentPath\":null}");
        expected.append(",");
        expected.append("{\"chartGrid\":\"B\",\"guid\":\"B\",\"hasChildren\":false,\"childrenNodes\":[],\"isSelected\":false,\"name\":\"B\",\"treeItemIsExpanded\":false,\"guiPath\":\"B\",\"availableCharts\":[],\"parentPath\":null}");
        //expected.append(",");
        //expected.append("{\"guid\":\"A:C\",\"isSelected\":false,\"name\":\"C\",\"treeItemIsExpanded\":false,\"guiPath\":\"A:C\",\"parentPath\":\"A\"}");
        expected.append("]}");

        JSONObject jsonObject = BuildJsonObjectsUtil.buildTreeTypeMenuJsonObject("treeMenuID", nodeList, 0, 20, false);
        assertEquals(expected.toString(), jsonObject.toString());
    }

    @Test
    public void test_that_third_level_nodes_generated_correct_json() throws JSONException {
        List<TreeMenuNode> nodeList = new ArrayList<TreeMenuNode>();
        nodeList.add(new TreeMenuNode("A", "Y"));
        nodeList.add(new TreeMenuNode("B", "Y"));
        nodeList.add(new TreeMenuNode("A:C", "Y"));
        nodeList.add(new TreeMenuNode("A:C:D", "Y"));

        StringBuilder expected = new StringBuilder();
        expected.append("{\"treeMenuID\":[");
        expected.append("{\"chartGrid\":\"A\",\"guid\":\"A\",\"hasChildren\":true,\"childrenNodes\":[\"A:C\"],\"isSelected\":false,\"name\":\"A\",\"treeItemIsExpanded\":false,\"guiPath\":\"A\",\"availableCharts\":[\"A:C\"],\"parentPath\":null}");
        expected.append(",");
        expected.append("{\"chartGrid\":\"A:C\",\"guid\":\"A:C\",\"hasChildren\":false,\"childrenNodes\":[],\"isSelected\":false,\"name\":\"C\",\"treeItemIsExpanded\":false,\"guiPath\":\"A:C\",\"availableCharts\":[\"A:C:D\"],\"parentPath\":\"A\"}");
        expected.append(",");
        expected.append("{\"chartGrid\":\"B\",\"guid\":\"B\",\"hasChildren\":false,\"childrenNodes\":[],\"isSelected\":false,\"name\":\"B\",\"treeItemIsExpanded\":false,\"guiPath\":\"B\",\"availableCharts\":[],\"parentPath\":null}");

        //expected.append(",");
        //expected.append("{\"guid\":4,\"isSelected\":false,\"name\":\"D\",\"treeItemIsExpanded\":false,\"guiPath\":\"A:C:D\",\"parentPath\":\"A:C\"}");
        expected.append("]}");

        JSONObject jsonObject = BuildJsonObjectsUtil.buildTreeTypeMenuJsonObject("treeMenuID", nodeList, 0, 20, false);
        assertEquals(expected.toString(), jsonObject.toString());
    }

    @Test
    public void test_that_third_level_nodes_without_parents_are_generated_correct_json() throws JSONException {
        List<TreeMenuNode> nodeList = new ArrayList<TreeMenuNode>();
        nodeList.add(new TreeMenuNode("A:B:C", "Y"));

        StringBuilder expected = new StringBuilder();
        expected.append("{\"treeMenuID\":[");
        expected.append("{\"chartGrid\":\"A\",\"guid\":\"A\",\"hasChildren\":true,\"childrenNodes\":[\"A:B\"],\"isSelected\":false,\"name\":\"A\",\"treeItemIsExpanded\":false,\"guiPath\":\"A\",\"availableCharts\":[],\"parentPath\":null}");
        expected.append(",");
        expected.append("{\"chartGrid\":\"A:B\",\"guid\":\"A:B\",\"hasChildren\":false,\"childrenNodes\":[],\"isSelected\":false,\"name\":\"B\",\"treeItemIsExpanded\":false,\"guiPath\":\"A:B\",\"availableCharts\":[\"A:B:C\"],\"parentPath\":\"A\"}");
        //expected.append(",");
        //expected.append("{\"guid\":3,\"isSelected\":false,\"name\":\"C\",\"treeItemIsExpanded\":false,\"guiPath\":\"A:B:C\",\"parentPath\":\"A:B\"}");
        expected.append("]}");

        JSONObject jsonObject = BuildJsonObjectsUtil.buildTreeTypeMenuJsonObject("treeMenuID", nodeList, 0, 20, false);
        assertEquals(expected.toString(), jsonObject.toString());
    }

    @Test
    public void test_that_deep_nodes_without_parents_generates_correct_json() throws JSONException {
        List<TreeMenuNode> nodeList = new ArrayList<TreeMenuNode>();
        nodeList.add(new TreeMenuNode("JSFlotJAgent:Custom:org.jsflot.components.BubbleDataPointComponent:<init>:Max Selftime", "Y"));

        StringBuilder expected = new StringBuilder();
        expected.append("{\"treeMenuID\":[");
        expected.append("{\"chartGrid\":\"JSFlotJAgent\",\"guid\":\"JSFlotJAgent\",\"hasChildren\":true,\"childrenNodes\":[\"JSFlotJAgent:Custom\"],\"isSelected\":false,\"name\":\"JSFlotJAgent\",\"treeItemIsExpanded\":false,\"guiPath\":\"JSFlotJAgent\",\"availableCharts\":[],\"parentPath\":null}");
        expected.append(",");
        expected.append("{\"chartGrid\":\"JSFlotJAgent:Custom\",\"guid\":\"JSFlotJAgent:Custom\",\"hasChildren\":true,\"childrenNodes\":[\"JSFlotJAgent:Custom:org.jsflot.components.BubbleDataPointComponent\"],\"isSelected\":false,\"name\":\"Custom\",\"treeItemIsExpanded\":false,\"guiPath\":\"JSFlotJAgent:Custom\",\"availableCharts\":[],\"parentPath\":\"JSFlotJAgent\"}");
        expected.append(",");
        expected.append("{\"chartGrid\":\"JSFlotJAgent:Custom:org.jsflot.components.BubbleDataPointComponent\",\"guid\":\"JSFlotJAgent:Custom:org.jsflot.components.BubbleDataPointComponent\",\"hasChildren\":true,\"childrenNodes\":[\"JSFlotJAgent:Custom:org.jsflot.components.BubbleDataPointComponent:<init>\"],\"isSelected\":false,\"name\":\"org.jsflot.components.BubbleDataPointComponent\",\"treeItemIsExpanded\":false,\"guiPath\":\"JSFlotJAgent:Custom:org.jsflot.components.BubbleDataPointComponent\",\"availableCharts\":[],\"parentPath\":\"JSFlotJAgent:Custom\"}");
        expected.append(",");
        expected.append("{\"chartGrid\":\"JSFlotJAgent:Custom:org.jsflot.components.BubbleDataPointComponent:<init>\",\"guid\":\"JSFlotJAgent:Custom:org.jsflot.components.BubbleDataPointComponent:<init>\",\"hasChildren\":false,\"childrenNodes\":[],\"isSelected\":false,\"name\":\"<init>\",\"treeItemIsExpanded\":false,\"guiPath\":\"JSFlotJAgent:Custom:org.jsflot.components.BubbleDataPointComponent:<init>\",\"availableCharts\":[\"JSFlotJAgent:Custom:org.jsflot.components.BubbleDataPointComponent:<init>:Max Selftime\"],\"parentPath\":\"JSFlotJAgent:Custom:org.jsflot.components.BubbleDataPointComponent\"}");
        //expected.append(",");
        //expected.append("{\"guid\":\"\",\"isSelected\":false,\"name\":\"Max Selftime\",\"treeItemIsExpanded\":false,\"guiPath\":\"JSFlotJAgent:Custom:org.jsflot.components.BubbleDataPointComponent:<init>:Max Selftime\",\"parentPath\":\"JSFlotJAgent:Custom:org.jsflot.components.BubbleDataPointComponent:<init>\"}");
        expected.append("]}");

        JSONObject jsonObject = BuildJsonObjectsUtil.buildTreeTypeMenuJsonObject("treeMenuID", nodeList, 0, 20, false);
        assertEquals(expected.toString(), jsonObject.toString());
    }

    @Test
    public void test_that_XYDataCollection_returns_correct_json() throws JSONException {
        XYDataSetCollection xyCollection = new XYDataSetCollection();
        XYDataList xyList = new XYDataList();
        xyList.setLabel("Set1");
        xyList.addDataPoint(new XYDataPoint(1, 1));
        xyList.addDataPoint(new XYDataPoint(2, 2));
        xyList.addDataPoint(new XYDataPoint(3, 3));
        xyList.addDataPoint(new XYDataPoint(5, 5));
        xyCollection.addDataList(xyList);

        StringBuilder expected = new StringBuilder();
        expected.append("{\"label\": \"Set1\", \"data\": [[1,1], [2,2], [3,3], [5,5]]}");
        String jsonString = BuildJsonObjectsUtil.generateChartData("chartId", "something", xyCollection);
        assertEquals(expected.toString(), jsonString);
    }
}
