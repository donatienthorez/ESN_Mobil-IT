package org.esn.mobilit.models;

import java.io.Serializable;
import java.util.List;

public class Guide implements Serializable {
    private String code_section;
    private boolean activated;
    private boolean created;
    private List<Node> nodes;

    public String getCode_section() {
        return code_section;
    }

    public void setCode_section(String code_section) {
        this.code_section = code_section;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public Node getNode(int i){
        return nodes.get(i);
    }
}

