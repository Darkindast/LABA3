
package mephi.b22901.a.l3;



import java.util.List;

public class Monster {
    private String name;
    private String type;
    private int danger;
    private String location;  
    private String firstMention; 
    private List<String> immunities;
    private Size size;
    private String activeTime; 
    private String vulnerability;
    private Recipe recipe;
    private String source;


    public String getName() { return name; }
    public String getType() { return type; }
    public int getDanger() { return danger; }
    public String getLocation() { return location; }
    public String getFirstMention() { return firstMention; }
    public List<String> getImmunities() { return immunities; }
    public Size getSize() { return size; }
    public String getActiveTime() { return activeTime; }
    public String getVulnerability() { return vulnerability; }
    public Recipe getRecipe() { return recipe; }
    public String getSource() { return source; }

    public void setSource(String source) { this.source = source; }
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setDanger(int danger) { this.danger = danger; }
    public void setLocation(String location) { this.location = location; }
    public void setFirstMention(String firstMention) { this.firstMention = firstMention; }
    public void setImmunities(List<String> immunities) { this.immunities = immunities; }
    public void setSize(Size size) { this.size = size; }
    public void setActiveTime(String activeTime) { this.activeTime = activeTime; }
    public void setVulnerability(String vulnerability) { this.vulnerability = vulnerability; }
    public void setRecipe(Recipe recipe) { this.recipe = recipe; }

    public static class Size {
        private double height_m;
        private double weight_kg;

        public double getHeight_m() { return height_m; }
        public double getWeight_kg() { return weight_kg; }
        public void setHeight_m(double height_m) { this.height_m = height_m; }
        public void setWeight_kg(double weight_kg) { this.weight_kg = weight_kg; }
    }


    public static class Recipe {
        private String type;
        private List<Ingredient> ingredients;
        private int time_min;
        private String effectiveness;

        public String getType() { return type; }
        public List<Ingredient> getIngredients() { return ingredients; }
        public int getTime_min() { return time_min; }
        public String getEffectiveness() { return effectiveness; }
        
        public void setType(String type) { this.type = type; }
        public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }
        public void setTime_min(int time_min) { this.time_min = time_min; }
        public void setEffectiveness(String effectiveness) { this.effectiveness = effectiveness; }

        public static class Ingredient {
            private String name;
            private int quantity;

            public String getName() { return name; }
            public int getQuantity() { return quantity; }
            public void setName(String name) { this.name = name; }
            public void setQuantity(int quantity) { this.quantity = quantity; }
        }
    }
}

