package me.starzebra.features.feature;

public class Route extends Feature{

    public Route() {
        super("Xray", 0, Category.RENDER);
    }

    @Override
    public void onEnable(){
        this.reload();
    }

    @Override
    public void onDisable(){
        this.reload();
    }

    private void reload() {
        mc.renderChunksMany = true;
        mc.renderGlobal.loadRenderers();
    }

}
