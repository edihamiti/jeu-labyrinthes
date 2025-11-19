package boutique.modele;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CosmetiqueTest {

    Cosmetique cosmetique = new Cosmetique("mur_brique", "Mur en brique", TypeCosmetique.TEXTURE_MUR, 100, "textures/mur_brique.png");

    @Test
    void id() {
        assertEquals("mur_brique", cosmetique.id());
        assertNotEquals("mur_gris", cosmetique.id());
    }

    @Test
    void nom() {
        assertEquals("Mur en brique", cosmetique.nom());
        assertNotEquals("Murdebrique", cosmetique.nom());
    }

    @Test
    void type() {
        assertEquals(TypeCosmetique.TEXTURE_MUR, cosmetique.type());
        assertNotEquals(TypeCosmetique.TEXTURE_JOUEUR, cosmetique.type());
    }

    @Test
    void prix() {
        assertEquals(100, cosmetique.prix());
        assertNotEquals(-1, cosmetique.prix());
    }

    @Test
    void cheminTexture() {
        assertEquals("textures/mur_brique.png", cosmetique.cheminTexture());
        assertNotEquals("textures/default/mur_brique.png", cosmetique.cheminTexture());
    }
}