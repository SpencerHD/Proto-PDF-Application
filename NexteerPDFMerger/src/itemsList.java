public class itemsList {

    private String name = null;
    private String size = null;
    private int pages = 0;
    private String date = null;

    public itemsList() {
    }

    public itemsList(String name, String size, int pages, String date) {
        this.name = name;
        this.size = size;
        this.pages = pages;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}