package com.movilizer.mds.webservice.models.maf;


public class MafCliMetaFile {
    private MafEventScript event;
    private MafLibraryScript library;
    private MafGenericScript generic;

    public MafCliMetaFile(MafEventScript event, MafLibraryScript library,
                          MafGenericScript generic) {
        this.event = event;
        this.library = library;
        this.generic = generic;
    }

    public MafSource getSource() {
        MafSource source;
        if (event != null) {
            source = event;
        } else if (library != null) {
            source = library;
        } else {
            source = generic;
        }
        return source;
    }
}
