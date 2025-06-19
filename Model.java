class BallModel {
   public double v = 2;
   public double vx, vy;
    //public int score = 0;

   public BallModel() {
      setzeGeschwindigkeit(v);
   }

   public void setzeGeschwindigkeit(double v) {
      double r = v * 5 + 5;
      double winkelGrad = Math.random() * 50 + 20;
      double winkelRad = Math.toRadians(winkelGrad);
      vx = r * Math.cos(winkelRad);
      vy = -r * Math.sin(winkelRad);

      if(Math.random() < 0.5) vx = -vx;
      if(Math.random() < 0.5) vy = -vy;
   }
}

class Block {
   int typ;
   Item item;

   Block(int typ, int itemtyp) {
      this.typ = typ;
      this.item = new Item(itemtyp);
   }

   public Item getItem() {
      return item;
   }

   public boolean istAktiv() {
      if(typ != 0 && typ != 5) {
         return true;
      }
      else{
         return false;
      }
   }

   public void zerstören() {
      typ = 0;
   }

   public void itemZerstören() {
      this.item.zerstören();
   }

   public int getTyp() {
      return typ;
   }
}

class BlockModel {
   public Block[][] matrix;
   public final int rows = 10;
   public final int cols = 10;

   BlockModel() {
      int[][] initMatrix = new int[][] {
         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
         { 1, 2, 3, 2, 4, 2, 3, 2, 1, 2 },
         { 2, 1, 2, 3, 2, 4, 2, 3, 2, 1 },
         { 1, 5, 3, 5, 4, 5, 3, 5, 1, 5 },
         { 2, 1, 2, 3, 2, 4, 2, 3, 2, 1 },
         { 5, 2, 3, 2, 5, 2, 3, 2, 5, 2 },
         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
      };

      int[][] initMatrixItems = new int[][] {
         { 2, 0, 0, 0, 3, 3, 0, 0, 0, 2 },
         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
         { 0, 1, 0, 0, 0, 0, 0, 0, 0, 1 },
         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
         { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
      };

      matrix = new Block[rows][cols];
      for (int y = 0; y < rows; y++) {
         for (int x = 0; x < cols; x++) {
            matrix[y][x] = new Block(initMatrix[y][x], initMatrixItems[y][x]);
         }
      }
   }

   public void zerstöreBlock(int x, int y) {
      matrix[y][x].zerstören();
   }

   public void zerstöreItem(int x, int y) {
      matrix[y][x].itemZerstören();
   }

   public int getBlockArt(int x, int y) {
      return matrix[y][x].getTyp();
   }

   public boolean sindAlleBlöckeZerstört() {
      return zaehleBlöcke() == 0;
   }

   public Item getBlockItem(int x, int y) {
      return matrix[y][x].getItem();
   }

   public int zaehleBlöcke() {
      int count = 0;
      for (int y = 0; y < rows; y++) {
         for (int x = 0; x < cols; x++) {
            if(matrix[y][x].istAktiv()) count++;
         }
      }
      return count; 
   }
}
