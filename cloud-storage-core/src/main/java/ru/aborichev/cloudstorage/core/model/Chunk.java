package ru.aborichev.cloudstorage.core.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class Chunk implements Serializable {
    private int chunkNumber;
    private byte[] data;
    private String md5;
    private int size;

}
