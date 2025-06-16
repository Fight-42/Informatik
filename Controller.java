class BallController extends Sprite {
   BallModel model;
   Schläger schläger;
   BlockModel blockModel;
   BlockView blockView;
   Schläger breite;
   static boolean itemActive = false;

   BallController(BallModel model, Schläger schläger, BlockModel blockModel, BlockView blockView) {
      super(390, 500, SpriteLibrary.Sleepy_Blocks, 7);
      setScale(0.3);
      this.model = model;
      this.schläger = schläger;
      this.blockModel = blockModel;
      this.blockView = blockView;
   }

   public void addEffect(int type) {
      BallController.itemActive = true;
      if(type == 1) {
         addAdditionalBall();
      }
      else if(type == 2) {
         makeBatLarger();
         schläger.setFillColor(Color.lime);
      }
      else if(type == 3) {
         makeBatSmaller();
         schläger.setFillColor(Color.orangered);
      }
      println("Effekt aktiviert. Typ = " + type, Color.lime);
   }

   public void addAdditionalBall() {
      BallController.itemActive = false;
      schläger.setFillColor(Color.white);
   }

   public void makeBatLarger() {
      schläger.makeLarger();
      MakeBatNormal normal = new MakeBatNormal(schläger);
      Timer.executeLater(normal, 10000);
   }

   public void makeBatSmaller() {
      schläger.makeSmaller();
      MakeBatNormal normal = new MakeBatNormal(schläger);
      Timer.executeLater(normal, 10000);
   }

   public void act() {
      if(isDestroyed()) return;

      double x = getCenterX();
      double y = getCenterY();

      if(y < getWidth() / 2) model.vy *= -1;
      if(x > 800 - getWidth() / 2 || x < getWidth() / 2) model.vx *= -1;
      if(y > 600 - getHeight() / 2) {
         Text title = new Text(400, 200, 100, "Game Over");
         title.setAlignment(Alignment.center);
         title.setFillColor(Color.orangered, 1);
         title.setBorderColor(Color.black);
         title.setStyle(true, false);
         title.setBorderWidth(7);
         destroy();
         System.exit(0);
      }

      if(collidesWith(schläger)) {
         double schlaegerXcorner = schläger.getCenterX() - 0.5 * schläger.getWidth(); //0.5 * breite
         double relativeX = (getCenterX() - schlaegerXcorner) / schläger.getWidth();  // aufteilen in Einheiten, die entsprechenden Winkel bekommen 
         double winkel = (relativeX - 0.5) * 150; // -75 bis +75 Grad
         double speed = Math.sqrt(model.vx * model.vx + model.vy * model.vy);
         double rad = Math.toRadians(winkel);
         model.vx = speed * Math.sin(rad);
         model.vy = -speed * Math.cos(rad);
         model.v += 0.05;
         
         println("relativeX: " + Math.round(relativeX) + ", winkel: " + Math.round(winkel));  //Test-ausgabe
      }

      int gridX = (int)(x / blockView.multiplierZ);
      int gridY = (int)(y / blockView.multiplierS);

      if(gridX >= 0 && gridX < blockModel.cols && gridY >= 0 && gridY < blockModel.rows) {
         if(blockModel.getBlockArt(gridX, gridY) != 0) {
            if(blockModel.getBlockArt(gridX, gridY) != 5) {
               blockModel.zerstöreBlock(gridX, gridY);
               blockView.zeichneBlöcke();
            }
            model.vy *= -1;

            println("Noch vorhandene Blöcke: " + blockModel.zaehleBlöcke());

            if(blockModel.sindAlleBlöckeZerstört()) {
               Text title = new Text(400, 200, 100, "You won!");
               title.setAlignment(Alignment.center);
               title.setFillColor(Color.greenyellow, 1);
               title.setBorderColor(Color.black);
               title.setBorderWidth(7);
               title.setStyle(true, false);
               destroy();
               System.exit(0);
            }
         }
         else if(blockModel.getBlockItem(gridX, gridY).getTyp() != 0 && BallController.itemActive == false) {
            int itemType = blockModel.getBlockItem(gridX, gridY).getTyp();
            blockModel.getBlockItem(gridX, gridY).zerstören();
            blockView.zeichneBlöcke();
            addEffect(itemType);
            
         }
      }

      move(model.vx, model.vy);
   }
}

class Schläger extends Rectangle {
   String links, rechts;
   double dy = 20;
   double normalBreite;
   double normalHöhe;

   Schläger(double x, double y, double breite, double höhe, String links, String rechts) {
      super(x, y, breite, höhe);
      this.links = links;
      this.rechts = rechts;
      this.normalBreite = breite;
      this.normalHöhe = höhe;
      setFillColor(Color.white);
   }

   public void act() {
      if(isKeyDown(links) && getCenterX() > getWidth() / 2) move(-dy, 0);
      if(isKeyDown(rechts) && getCenterX() < 800 - getWidth() / 2) move(dy, 0);
   }

   public void makeSmaller() {
      this.setWidth(normalBreite - 30);
      this.setHeight(normalHöhe);
   }

   public void makeNormal() {
      this.setWidth(normalBreite);
      this.setHeight(normalHöhe);
   }

   public void makeLarger() {
      this.setWidth(normalBreite + 50);
      this.setHeight(normalHöhe);
   }
}

class MakeBatNormal implements Runnable {
   Schläger bat;

   MakeBatNormal(Schläger bat) {
      this.bat = bat;
   }

  	public void run() {
      this.bat.makeNormal();
      BallController.itemActive = false;
      bat.setFillColor(Color.white);
      println("Effekt abgelaufen.", Color.red);
  	}
}
