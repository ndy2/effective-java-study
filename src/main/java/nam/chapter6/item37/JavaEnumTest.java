package nam.chapter6.item37;

import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaEnumTest {

    enum Type {A, B, C}

    @Test
    void ordinal() {
        assertThat(Type.A.ordinal()).isEqualTo(0);
        assertThat(Type.B.ordinal()).isEqualTo(1);
        assertThat(Type.C.ordinal()).isEqualTo(2);
    }


    enum TypeWithIndex {
        B(1), A(0), C(2);

        int index;

        TypeWithIndex(int index) {
            this.index = index;
        }
    }

    @Test
    void field() {
        assertThat(TypeWithIndex.A.index).isEqualTo(0);
        assertThat(TypeWithIndex.B.index).isEqualTo(1);
        assertThat(TypeWithIndex.C.index).isEqualTo(2);
    }

    @Test
    void enumMap() {
        // given
        EnumMap<Type, Object> enumMap = new EnumMap<>(Type.class);

        // when
        for (Type t : Type.values()) {
            enumMap.put(t, t.toString() + t.toString());
        }

        // then
        assertThat(enumMap).containsExactlyInAnyOrderEntriesOf(Map.of(
                Type.A, "AA",
                Type.B, "BB",
                Type.C, "CC"
        ));
    }

    enum EnumWithToString {
        A, B, C;


        @Override
        public String toString() {
            return super.toString() + "_Some_User_Friendly_Name";
        }
    }

    @Test
    void toStringTest() {
        assertThat(EnumWithToString.A.toString()).isEqualTo("A_Some_User_Friendly_Name");
        assertThat(EnumWithToString.B.toString()).isEqualTo("B_Some_User_Friendly_Name");
        assertThat(EnumWithToString.C.toString()).isEqualTo("C_Some_User_Friendly_Name");
    }
}
