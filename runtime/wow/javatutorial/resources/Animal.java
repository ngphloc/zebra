public class Animal {
		private String color;
		
		public void setColor(String color) {
			this.color = color;
		}
    
		public String getColor() {
			return this.color;
		}
     public static void hide() {
        System.out.println("The hide method in Animal.");
    }
    public void override() {
        System.out.println("The override method in Animal.");
    }
}
