class BallModel {
   public double v = 2;
   public double vx, vy;

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


   BlockModel(int[][] blockData, int[][] itemData) {
      matrix = new Block[rows][cols];
      for (int y = 0; y < rows; y++) {
         for (int x = 0; x < cols; x++) {
            matrix[y][x] = new Block(blockData[y][x], itemData[y][x]);
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

public class LevelData {

   public static final int LEVEL_COUNT = 5;

   public static int[][] getBlockLevel(int level) {
      switch(level) {
         case 1 : return level1Blocks;
         case 2 : return level2Blocks;
         case 3 : return level3Blocks;
         case 4 : return level4Blocks;
         case 5 : return level5Blocks;
      }
   }

   public static int[][] getItemLevel(int level) {
      switch(level) {
         case 1 : return level1Items;
         case 2 : return level2Items;
         case 3 : return level3Items;
         case 4 : return level4Items;
         case 5 : return level5Items;
      }
   }

    // Beispiel-Leveldaten (du kannst sie auffüllen oder anpassen)
   private static final int[][] level1Blocks = {
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 1, 2, 3, 2, 4, 2, 3, 2, 1, 2 },
      { 2, 1, 2, 3, 2, 4, 2, 3, 2, 1 },
      { 1, 5, 3, 1, 4, 5, 3, 5, 1, 3 },
      { 2, 1, 2, 3, 2, 4, 2, 3, 2, 1 },
      { 0, 2, 3, 2, 5, 2, 3, 2, 0, 2 },
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
   };

   private static final int[][] level1Items = {
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 2, 0, 0, 0, 0, 0, 0, 0, 1 },
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
      { 0, 2, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
   };


   private static final int[][] level2Blocks = {
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 1, 2, 3, 2, 4, 2, 3, 2, 1, 2 },
      { 2, 1, 2, 3, 2, 4, 2, 3, 2, 1 },
      { 1, 5, 3, 1, 4, 5, 3, 5, 1, 5 },
      { 2, 1, 2, 3, 2, 4, 2, 3, 2, 1 },
      { 1, 2, 3, 2, 5, 2, 3, 2, 1, 2 },
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
   };

   private static final int[][] level2Items = {
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
    
   private static final int[][] level3Blocks = {
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

   private static final int[][] level3Items = {
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
    
   private static final int[][] level4Blocks = {
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 1, 2, 3, 2, 4, 2, 3, 2, 1, 2 },
      { 2, 1, 2, 3, 2, 4, 2, 3, 2, 1 },
      { 1, 5, 3, 5, 4, 5, 3, 5, 1, 5 },
      { 2, 1, 2, 3, 2, 4, 2, 3, 2, 1 },
      { 0, 2, 3, 2, 5, 2, 3, 2, 0, 2 },
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
   };

   private static final int[][] level4Items = {
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
    
     private static final int[][] level5Blocks = {
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 1, 2, 3, 2, 4, 2, 3, 2, 1, 2 },
        { 2, 1, 2, 3, 2, 4, 2, 3, 2, 1 },
        { 1, 5, 3, 5, 4, 5, 3, 5, 1, 5 },
        { 2, 1, 2, 3, 2, 4, 2, 3, 2, 1 },
        { 0, 2, 3, 2, 5, 2, 3, 2, 0, 2 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
    };

    private static final int[][] level5Items = {
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
}
