package com.scaling.libraryservice.util;

import com.scaling.libraryservice.entity.Library;
import java.util.List;

public interface LibraryFinder {

    List<Library> findAroundLibraries(Location location);

}
