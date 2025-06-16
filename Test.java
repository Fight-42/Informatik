@Test 
class Blockzahl {
   
   @Test
   void testeBlockZahl() {
      BlockModel testModel = new BlockModel();
      assertEquals(37,testModel.zaehleBlöcke(),"falsche Blockanzahl");  
   }
}
