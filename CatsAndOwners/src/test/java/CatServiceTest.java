import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.Chub.DTOs.CatBreedDto;
import com.Chub.DTOs.CatColorDto;
import com.Chub.DTOs.CatDTO;
import com.Chub.Services.CatMapperDTO;
import com.Chub.Services.CatService;
import com.Chub.dao.Models.cats.Cat;
import com.Chub.dao.Models.cats.CatBreed;
import com.Chub.dao.Models.cats.CatColor;
import com.Chub.dao.Models.owners.Owner;
import com.Chub.dao.repositories.CatRepository;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

public class CatServiceTest {
    private final CatRepository _hibernate;
    private final Owner _owner;
    private final Cat _cat;
    public CatServiceTest() {
        _owner = new Owner(1545454L, "Dan", LocalDate.of(2004, 8, 5));
        _hibernate = mock(CatRepository.class);
        _cat = new Cat(1234L, "Kek", LocalDate.of(2023,4,5), CatBreed.AmericanShorthair, CatColor.Black, _owner);
        when(_hibernate.findById(1234L)).thenReturn(Optional.of(_cat));
    }

    @Test
    public void AddCatExceptionTest() {
        CatService service = new CatService(_hibernate);
        CatMapperDTO catMapperDTO = new CatMapperDTO();
        try {
            service.AddCat(catMapperDTO.MappedToCatDTO(_cat));
            fail();
        } catch (Exception ignored) {}
    }

    @Test
    public void RemoveCatExceptionTest() {
        CatService service = new CatService(_hibernate);
        CatMapperDTO catMapperDTO = new CatMapperDTO();
        Cat cat2 = new Cat(12345L, "Kitty", LocalDate.now(), CatBreed.ScottishFold, CatColor.Grey, _owner);
        when(_hibernate.findById(12345L)).thenReturn(Optional.empty());
        try {
            service.RemoveCat(catMapperDTO.MappedToCatDTO(cat2));
            fail();
        } catch (Exception ignored) {}
    }

    @Test
    public void FindCatServiceTest() {
        CatService service = new CatService(_hibernate);
        CatDTO catDTO = new CatDTO();
        catDTO.setID(1234L);
        catDTO.setName("Kek");
        catDTO.setColor(CatColorDto.Black);
        catDTO.setBreed(CatBreedDto.AmericanShorthair);
        catDTO.setBirthDate(LocalDate.of(2023,4,5));

        assertEquals(catDTO.getID(), service.FindCatByID(1234L).getID());
    }

    @Test
    public void MakeFriendServiceTest() throws Exception {
        Cat cat2 = new Cat(12345L, "Kitty", LocalDate.now(), CatBreed.ScottishFold, CatColor.Grey, _owner);
        when(_hibernate.findById(12345L)).thenReturn(Optional.of(cat2));
        CatService service = new CatService(_hibernate);
        doAnswer(invocation -> {
            _cat.MakeFriend(cat2);
            cat2.MakeFriend(_cat);
            return null;
        }).when(_hibernate).save(any());
        service.MakeFriends(_cat.getId(), cat2.getId());

        assertTrue(service.GetFriends(_cat.getId()).stream().anyMatch(cat -> cat.getID().equals(cat2.getId())));
        assertTrue(service.GetFriends(cat2.getId()).stream().anyMatch(cat -> cat.getID().equals(_cat.getId())));

        try {
            service.MakeFriends(_cat.getId(), cat2.getId());
            fail();
        } catch (Exception ignored) {}
    }
}


