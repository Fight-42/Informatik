class Item {
    int typ;

    Item(int typ) {
        this.typ = typ;
    }

    public boolean istAktiv() {
        return typ != 0;
    }

    public void zerstören() {
        typ = 0;
    }

    public int getTyp() {
        return typ;
    }
}
