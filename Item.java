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

// Repräsentiert das sichtbare Power-Up/Item, das fällt
class FallendesItem extends Sprite {
   int typ;
   double speed = 1.5;
   boolean eingesammelt = false;

   FallendesItem(double x, double y, int typ) {
      super(x, y, SpriteLibrary.Items, typ - 1); // Sprite für Item
      this.typ = typ;
      setScale(1);
   }

   public void act() {
      if(eingesammelt || isDestroyed()) return;

      move(0, speed);

        // Unteren Rand prüfen
      if(getCenterY() > 700) {
         destroy();
      }
   }

   public void einsammeln() {
      eingesammelt = true;
      destroy();
   }

   public int getTyp() {
      return typ;
   }
}
