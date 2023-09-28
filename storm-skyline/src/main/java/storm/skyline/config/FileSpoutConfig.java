package storm.skyline.config;

import java.io.Serializable;

public class FileSpoutConfig implements Serializable {

    private static final long serialVersionUID = 5039447436011649516L;

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
