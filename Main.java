public class GameController extends Actor {
   private ArrayList<BallController> bälle;
   private Schläger schläger;
   private BlockModel blockModel;
   private BlockView blockView;

   public GameController() {
        // Initialisiere BlockModel, BlockView, Schläger
      blockModel = new BlockModel();
      blockView = new BlockView(blockModel, 80, 60);
      schläger = new Schläger(360, 580, 120, 20, "a", "d");

        // Multi-Ball-Verwaltung
      bälle = new ArrayList<BallController>();
      BallController ersterBall = new BallController(new BallModel(), schläger, blockModel, blockView, this);
      bälle.add(ersterBall);
   }

   public void act() {

        // Von hinten nach vorne iterieren, falls Bälle entfernt werden
      for (int i = bälle.size() - 1; i >= 0; i--) {
         BallController ball = bälle.get(i);
         if(ball.isDestroyed()) {
            bälle.remove(i);
         }
      }

      if(bälle.isEmpty()) {
            // Zeige "Game Over"-Text
         Text title = new Text(400, 200, 100, "Game Over");
         title.setAlignment(Alignment.center);
         title.setFillColor(Color.orangered, 1);
         title.setBorderColor(Color.black);
         title.setStyle(true, false);
         title.setBorderWidth(7);
         System.exit(0);
      }
   }

   public void addBall(BallController neuerBall) {
      bälle.add(neuerBall);
   }
}
new World(800,680);
new GameController(); 
