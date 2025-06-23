public class BallController extends Sprite {
   BallModel model;
   Schläger schläger;
   BlockModel blockModel;
   BlockView blockView;
   GameController spiel;
   static boolean itemActive = false;
   boolean gestartet = false;


   public BallController(BallModel model, Schläger schläger, BlockModel blockModel, BlockView blockView, GameController spiel) {
      super(390, 500, SpriteLibrary.Sleepy_Blocks, 7);
      setScale(0.3);
      this.model = model;
      this.schläger = schläger;
      this.blockModel = blockModel;
      this.blockView = blockView;
      this.spiel = spiel;

    //ball mittig auf schläger
      double ballX = schläger.getCenterX();
      double ballY = schläger.getCenterY() - schläger.getHeight() / 2 - getHeight() / 2;
      moveTo(ballX, ballY);
   }


   public void addEffect(int type) {
      BallController.itemActive = true;
      Sound.playSound(Sound.short_bell);
      if(type == 1) {
         addAdditionalBall();
      } else if(type == 2) {
         makeBatLarger();
         schläger.setFillColor(Color.lime);
      } else if(type == 3) {
         makeBatSmaller();
         schläger.setFillColor(Color.orangered);
      }
      println("Effekt aktiviert. Typ = " + type, Color.lime);
   }

   public void addAdditionalBall() {
      BallController.itemActive = false;
      schläger.setFillColor(Color.white);

      BallModel neuesModel = new BallModel();
      neuesModel.setzeGeschwindigkeit(model.v);
      BallController neuerBall = new BallController(neuesModel, schläger, blockModel, blockView, spiel);
      neuerBall.moveTo(this.getCenterX() + 10, this.getCenterY() + 10);

      spiel.addBall(neuerBall);
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
      
      if (!gestartet) {
        // Ball bleibt auf dem Schläger
        double ballX = schläger.getCenterX();
        double ballY = schläger.getCenterY() - schläger.getHeight() / 2 - getHeight() / 2;
        moveTo(ballX, ballY);

        // Starte Ball bei Tastendruck "a" oder "d"
        if (isKeyDown("a") || isKeyDown("d")) {
            gestartet = true;

            // Zufällige Richtung nach oben
            double winkelGrad = Math.random() * 50 + 20; // 20° bis 70°
            if (Math.random() < 0.5) winkelGrad = 180 - winkelGrad; // nach links oder rechts

            double winkelRad = Math.toRadians(winkelGrad);
            double speed = model.v * 5 + 5;
            model.vx = speed * Math.cos(winkelRad);
            model.vy = -speed * Math.sin(winkelRad);
        }

        return; // solange nicht gestartet, keine weitere Bewegung
    }

      lookAheadAndMove();

      if(getCenterY() > 600 - getHeight() / 2) {
         destroy();
         return;
      }
   }
   
   private void lookAheadAndMove() {
      double nextX = getCenterX() + model.vx;
      double nextY = getCenterY() + model.vy;

      boolean xKollidiert = checkBlockCollision(nextX, getCenterY());
      boolean yKollidiert = checkBlockCollision(getCenterX(), nextY);

      // Seitenrand prüfen (x)
      if(nextX <= getWidth() / 2 || nextX >= 800 - getWidth() / 2) {
         model.vx *= -1;
         nextX = getCenterX(); // Ball bleibt auf gleicher x-Position
      }
      // Oberer Rand prüfen (y)
      if(nextY <= getHeight() / 2) {
         model.vy *= -1;
         nextY = getCenterY(); // Ball bleibt auf gleicher y-Position
      }

      // Blockkollision – X-Richtung
      if(xKollidiert) {
         model.vx *= -1;
         Sound.playSound(Sound.boulder);
      }
      // Blockkollision – Y-Richtung
      if(yKollidiert) {
         model.vy *= -1;
         Sound.playSound(Sound.boulder);
      }

      // Schläger-Kollision
      if(collidesWithSchlaeger(nextX, nextY)) {
         double schlaegerXcorner = schläger.getCenterX() - 0.5 * schläger.getWidth();
         double relativeX = (getCenterX() - schlaegerXcorner) / schläger.getWidth();
         double winkel = (relativeX - 0.5) * 150;
         double speed = Math.sqrt(model.vx * model.vx + model.vy * model.vy);
         double rad = Math.toRadians(winkel);
         model.vx = speed * Math.sin(rad);
         model.vy = -speed * Math.cos(rad);
         model.v += 0.05;
         if(winkel > 0) Sound.playSound(Sound.pong_f);
         else Sound.playSound(Sound.pong_d);
         // println("relativeX: " + Math.round(relativeX) + ", winkel: " + Math.round(winkel));
      }

      // PowerUp/BlockItem prüfen
      int gridX = (int)(getCenterX() / blockView.multiplierZ);
      int gridY = (int)(getCenterY() / blockView.multiplierS);
      if(gridX >= 0 && gridX < blockModel.cols && gridY >= 0 && gridY < blockModel.rows) {
         if(blockModel.getBlockItem(gridX, gridY).getTyp() != 0 && BallController.itemActive == false) {
            int itemType = blockModel.getBlockItem(gridX, gridY).getTyp();
            blockModel.getBlockItem(gridX, gridY).zerstören();
            blockView.zeichneBlöcke();

            FallendesItem item = new FallendesItem(getCenterX(), getCenterY(), itemType);
            spiel.addItem(item);      // zur Liste im Controller hinzufügen

         }
      }

      move(model.vx, model.vy);
   }

   // Prüft, ob an der Position (px, py) eine Blockkollision stattfindet,
   // zerstört ggf. den Block (außer Blockart 5), gibt true zurück falls Block
   private boolean checkBlockCollision(double px, double py) {
      int gridX = (int)(px / blockView.multiplierZ);
      int gridY = (int)(py / blockView.multiplierS);

      if(gridX >= 0 && gridX < blockModel.cols && gridY >= 0 && gridY < blockModel.rows) {
         if(blockModel.getBlockArt(gridX, gridY) != 0) {
            if(blockModel.getBlockArt(gridX, gridY) != 5) {
               blockModel.zerstöreBlock(gridX, gridY); 
               Item blockItem = blockModel.getBlockItem(gridX, gridY);
               if(blockItem.getTyp() != 0) {
                  FallendesItem neuesItem = new FallendesItem(
                     gridX * blockView.multiplierZ + blockView.multiplierZ / 2.0,
                     gridY * blockView.multiplierS + blockView.multiplierS / 2.0,
                     blockItem.getTyp()
                     );
                  spiel.addItem(neuesItem);
                  blockModel.zerstöreItem(gridX, gridY); // Entferne das Item im Modell
               }

               blockView.zeichneBlöcke();
            }
            // println("Noch vorhandene Blöcke: " + blockModel.zaehleBlöcke());
            if(blockModel.sindAlleBlöckeZerstört()) {
               Text title = new Text(400, 200, 100, "You won!");
               title.setAlignment(Alignment.center);
               title.setFillColor(Color.greenyellow, 1);
               title.setBorderColor(Color.black);
               title.setBorderWidth(7);
               title.setStyle(true, false);
               setImage(SpriteLibrary.Ballskin, 0);
               System.exit(0);
            }
            return true;
         }
      }
      return false;
   }

   private boolean collidesWithSchlaeger(double nx, double ny) {
      // Temporär Ball an neue Position setzen und mit Schläger prüfen
      double oldX = getCenterX();
      double oldY = getCenterY();
      moveTo(nx, ny);
      boolean collides = collidesWith(schläger);
      moveTo(oldX, oldY); // zurücksetzen
      return collides;
   }
}

// --- Rest unverändert ---
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
