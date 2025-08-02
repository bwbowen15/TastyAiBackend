package dev.brian.TastyAiBackend;

public class ChatRequest {
    private String recipe;
    private String restrictions;
    private String goals;

    //Getters and Setters
    public String getRecipe(){
        return recipe;
    }
    public void setRecipe(String recipe){
        this.recipe = recipe;
    }

    public String getRestrictions(){
        return restrictions;
    }
    public void setRestrictions(String restrictions){
        this.restrictions = restrictions;
    }

    public String getGoals(){
        return goals;
    }
    public void setGoals(String goals) {
        this.goals = goals;
    }


}
