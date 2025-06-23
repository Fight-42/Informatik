class BlockView extends Actor {
   BlockModel model;
   Group allBlocks;
   int multiplierZ, multiplierS;
   final double blockGap = 4; // Abstand zwischen den Blöcken
   final double cornerRadius = 10; // Radius der abgerundeten Ecken

   BlockView(BlockModel model, int multiplierZ, int multiplierS) {
      this.model = model;
      this.multiplierZ = multiplierZ;
      this.multiplierS = multiplierS;
      this.allBlocks = new Group();
      zeichneBlöcke();
   }

   public void zeichneBlöcke() {
      allBlocks.destroyAllChildren();
      for (int y = 0; y < model.rows; y++) {
         for (int x = 0; x < model.cols; x++) {
            int typ = model.getBlockArt(x, y);
            if(typ > 0) {
               Color farbe = gibFarbe(typ);

                    // Block-Position und -Größe berechnen mit Abstand
               double bx = x * multiplierZ + blockGap / 2.0;
               double by = y * multiplierS + blockGap / 2.0;
               double bw = multiplierZ - blockGap;
               double bh = multiplierS - blockGap;
               double r = Math.min(cornerRadius, Math.min(bw, bh) / 2.0);

                    // Gruppe für den Block anlegen
               Group abgerundeterBlock = erstelleAbgerundetenBlock(bx, by, bw, bh, r, farbe);
               allBlocks.add(abgerundeterBlock);
            } else if(typ == 0) {
               if(model.getBlockItem(x, y).getTyp() > 0) {
                  Sprite itemNeu = new Sprite((x * multiplierZ + blockGap / 2.0) + (multiplierZ / 2.0), (y * multiplierS + blockGap / 2.0) + (multiplierS / 2.0), SpriteLibrary.Items, model.getBlockItem(x, y).getTyp() - 1);
                  allBlocks.add(itemNeu);
               }
            }
         }
      }
   }

    /**
     * Erstellt eine Gruppe aus 4 Kreisen (Ecken) und 2 Rechtecken (zwischen den Kreisen)
     */
   private Group erstelleAbgerundetenBlock(double x, double y, double breite, double hoehe, double radius, Color farbe) {
      Group block = new Group();

        // Rechtecke (horizontal und vertikal)
      Rectangle horizontal = new Rectangle(
         x + radius, y,
         breite - 2 * radius, hoehe
         );
      horizontal.setFillColor(farbe);

      Rectangle vertikal = new Rectangle(
         x, y + radius,
         breite, hoehe - 2 * radius
         );
      vertikal.setFillColor(farbe);

        // Ecken (Kreise)
      Circle obenLinks = new Circle(x + radius, y + radius, radius);
      obenLinks.setFillColor(farbe);
      Circle obenRechts = new Circle(x + breite - radius, y + radius, radius);
      obenRechts.setFillColor(farbe);
      Circle untenLinks = new Circle(x + radius, y + hoehe - radius, radius);
      untenLinks.setFillColor(farbe);
      Circle untenRechts = new Circle(x + breite - radius, y + hoehe - radius, radius);
      untenRechts.setFillColor(farbe);

        // Zur Gruppe hinzufügen
      block.add(horizontal);
      block.add(vertikal);
      block.add(obenLinks);
      block.add(obenRechts);
      block.add(untenLinks);
      block.add(untenRechts);

      return block;
   }

   private Color gibFarbe(int typ) {
      switch(typ) {
         case 1 : return Color.orangered;
         case 2 : return Color.violet;
         case 3 : return Color.yellow;
         case 4 : return Color.blue;
         case 5 : return Color.gray;
         default : return Color.black;
      }
   }
}
