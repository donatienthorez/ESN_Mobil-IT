package org.esn.mobilit.renderers;

import java.util.List;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.R;
import org.esn.mobilit.models.Guide;
import org.esn.mobilit.models.Node;

public class GuideRenderer {

    public String renderSurvivalGuide(Guide guide)
    {
        return computeNodes(guide.getNodes(), 0);
    }

    public String computeNodes(List<Node> nodes, int level)
    {
        String content = "";
        for (Node node : nodes) {
            content = content.concat(computeNode(node, level));
            content = content.concat(computeNodes(node.getNodes(), level + 1));
        }

        return content;
    }

    public String computeNode(Node node, int level)
    {
        String title = "<h" + (level + 1) + "><font color='"
                + getColorByCategoryLevel(level) + "'>"
                + node.getTitle() + "</font><h" + (level + 1)
                + "><br/>";

        String content = "<p><font color='"
                + getColorByCategoryLevel(3)+ "'>"
                + node.getContent() + "</font></<br/>";

        return title.concat(content);
    }

    public String getColorByCategoryLevel(int level)
    {
        switch (level){
            case 0 : return String.valueOf(MobilITApplication.getContext().getResources().getColor(R.color.esngreen));
            case 1 : return String.valueOf(MobilITApplication.getContext().getResources().getColor(R.color.esnpink));
            case 2 : return String.valueOf(MobilITApplication.getContext().getResources().getColor(R.color.esnorange));
            default : return String.valueOf(MobilITApplication.getContext().getResources().getColor(R.color.esngrey));
        }
    }
}
