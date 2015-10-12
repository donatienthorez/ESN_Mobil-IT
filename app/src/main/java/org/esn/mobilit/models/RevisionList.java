package org.esn.mobilit.models;

import java.util.List;

public class RevisionList {

    private List<Revision> revision;

    public RevisionList(List<Revision> revisionList) {
        this.revision = revisionList;
    }

    public List<Revision> getRevisionList() {
        return revision;
    }

    public void setRevisionList(List<Revision> revisionList) {
        this.revision = revisionList;
    }

    public Revision getRevision(int i){
        return revision.get(i);
    }

    @Override
    public String toString() {
        String toString = "";
        for (Revision rev:revision){
            toString += rev.toString();
        }
        return toString;
    }
}
