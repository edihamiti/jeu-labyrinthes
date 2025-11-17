package defi.repository;

import defi.modele.Defi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DefisRepo {

    private final List<Defi> defisRepo;

    public DefisRepo() {
        this.defisRepo = new ArrayList<>();
    }

    public void ajouterDefi(Defi defi) {
        defisRepo.add(defi);
    }

    public HashMap<Defi, Boolean> getMapsDefis() {
        HashMap<Defi, Boolean> progression = new HashMap<>();
        defisRepo.forEach(defi -> progression.put(defi, false));
        return progression;
    }

    public List<Defi> getDefisRepo() {
        return defisRepo;
    }
}
