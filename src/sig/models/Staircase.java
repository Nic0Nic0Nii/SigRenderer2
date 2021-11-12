package sig.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sig.Block;
import sig.BlockType;
import sig.FacingDirection;
import sig.Mesh;
import sig.SigRenderer;
import sig.Triangle;
import sig.utils.OBJReader;

public class Staircase extends Mesh{
    public Staircase(BlockType type) {
        super(type);
        this.type = type;
        this.triangles=OBJReader.ReadOBJFile("models/staircase.obj",true);
        for (Triangle t : triangles) {
            t.tex=type.getTexture(BlockType.FRONT);
        }
       triangles.get(0).dir=BlockType.FRONT;
       triangles.get(1).dir=BlockType.FRONT;
       triangles.get(2).dir=BlockType.TOP;
       triangles.get(3).dir=BlockType.TOP;
       triangles.get(4).dir=BlockType.TOP;
       triangles.get(5).dir=BlockType.TOP;
       triangles.get(6).dir=BlockType.FRONT;
       triangles.get(7).dir=BlockType.FRONT;
       triangles.get(8).dir=BlockType.BACK;
       triangles.get(9).dir=BlockType.BACK;
       triangles.get(10).dir=BlockType.BOTTOM;
       triangles.get(11).dir=BlockType.BOTTOM;
       triangles.get(12).dir=BlockType.LEFT;
       triangles.get(13).dir=BlockType.LEFT;
       triangles.get(14).dir=BlockType.LEFT;
       triangles.get(15).dir=BlockType.LEFT;
       triangles.get(16).dir=BlockType.RIGHT;
       triangles.get(17).dir=BlockType.RIGHT;
       triangles.get(18).dir=BlockType.RIGHT;
       triangles.get(19).dir=BlockType.RIGHT;
    }
    
    protected List<Triangle> prepareRender(Block b) {
        List<Triangle> tris = new ArrayList<Triangle>();

        Triangle[][] renderTriangles = new Triangle[][]{
            {b.block.triangles.get(0),b.block.triangles.get(1)},
            {b.block.triangles.get(12),b.block.triangles.get(13),b.block.triangles.get(14),b.block.triangles.get(15)},
            {b.block.triangles.get(8),b.block.triangles.get(9)},
            {b.block.triangles.get(16),b.block.triangles.get(17),b.block.triangles.get(18),b.block.triangles.get(19)},
        };
        if (!b.neighbors.UP) {
            tris.add(b.block.triangles.get(4));
            tris.add(b.block.triangles.get(5));
        }
        if (!b.neighbors.DOWN) {
            tris.add(b.block.triangles.get(10));
            tris.add(b.block.triangles.get(11));
        }
        if (!b.neighbors.LEFT) {
            tris.addAll(Arrays.asList(renderTriangles[(b.getFacingDirection().ordinal()+FacingDirection.WEST.ordinal())%4]));
        }
        if (!b.neighbors.RIGHT) {
            tris.addAll(Arrays.asList(renderTriangles[(b.getFacingDirection().ordinal()+FacingDirection.EAST.ordinal())%4]));
        }
        if (!b.neighbors.FORWARD) {
            tris.addAll(Arrays.asList(renderTriangles[(b.getFacingDirection().ordinal()+FacingDirection.NORTH.ordinal())%4]));
        }
        if (!b.neighbors.BACKWARD) {
            tris.addAll(Arrays.asList(renderTriangles[(b.getFacingDirection().ordinal()+FacingDirection.SOUTH.ordinal())%4]));
        }   
        tris.add(b.block.triangles.get(2));
        tris.add(b.block.triangles.get(3));
        tris.add(b.block.triangles.get(6));
        tris.add(b.block.triangles.get(7));
        return tris;
    }
    public boolean handleCollision(Block b) {
        if (SigRenderer.currentStaircase!=null) {
            float diffX=SigRenderer.vCamera.x-b.pos.x;
            float diffZ=SigRenderer.vCamera.z-b.pos.z;
            switch (b.getFacingDirection()) {
                case EAST: {
                    SigRenderer.vCamera.y=b.pos.y+diffX+0.7f;
                }break;
                case NORTH: {
                    SigRenderer.vCamera.y=b.pos.y+(1-diffZ)+0.7f;
                }break;
                case SOUTH: {
                    SigRenderer.vCamera.y=b.pos.y+diffZ+0.7f;
                }break;
                case WEST: {
                    SigRenderer.vCamera.y=b.pos.y+(1-diffX)+0.7f;
                }break;
            }
            SigRenderer.fallSpd=0;
            return true;
        }
        if (SigRenderer.vCamera.y>=b.pos.y) {
            boolean valid=false;
            if (SigRenderer.vCamera.y>=b.pos.y+1f) {
                valid=true;
            } else {
                switch (b.getFacingDirection()) {
                    case EAST: {
                        if (SigRenderer.vCamera.x<b.pos.x+0.5f) {
                            valid=true;
                        }
                    }break;
                    case NORTH: {
                        if (SigRenderer.vCamera.z>b.pos.z+0.5f) {
                            valid=true;
                        }
                    }break;
                    case SOUTH: {
                        if (SigRenderer.vCamera.z<b.pos.z+0.5f) {
                            valid=true;
                        }
                    }break;
                    case WEST: {
                        if (SigRenderer.vCamera.x>b.pos.x+0.5f) {
                            valid=true;
                        }
                    }break;
                }
            }
            if (valid) {
                SigRenderer.currentStaircase=this;
                return true;
            }
        }
        return false;
    }
}
