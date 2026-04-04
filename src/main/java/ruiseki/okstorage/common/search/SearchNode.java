package ruiseki.okstorage.common.search;

public interface SearchNode {

    boolean matches(ItemStackKey key);
}
