class MainBreakinGame extends Actor {
   BallModel ballModel;
   BlockModel blockModel;
   BlockView blockView;
   BallController ballController;
   Schläger schläger;

   MainBreakinGame() {
      blockModel = new BlockModel();
      blockView = new BlockView(blockModel, 80, 60);
      schläger = new Schläger(360, 580, 120, 20, "a", "d");
      ballModel = new BallModel();
      ballController = new BallController(ballModel, schläger, blockModel, blockView);
   }
}

new MainBreakinGame();
