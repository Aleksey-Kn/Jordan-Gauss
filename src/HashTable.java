import java.util.*;

public class HashTable<Key, Value> implements Map<Key, Value> {
    private int size;
    private TableEntry<Key, Value>[] table;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        for(TableEntry<Key, Value> entry: table){
            if(!(entry == null || entry.isDeleted)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean containsKey(Object key) {
        int idx = findKey((Key)key);
        if (idx == -1 || table[idx] == null || table[idx].isDeleted) {
            return false;
        }
        return true;
    }

    @Override
    public boolean containsValue(Object value) {
        for(TableEntry<Key, Value> entry: table){
            if(entry != null && !entry.isDeleted && entry.value.equals(value)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Value remove(Object key) {
        int ind = findKey((Key)key);
        if(ind == -1 || table[ind] == null || table[ind].isDeleted){
            return null;
        }
        table[ind].isDeleted = true;
        return table[ind].value;
    }

    @Override
    public void putAll(Map<? extends Key, ? extends Value> m) {
        for(Map.Entry<? extends Key, ? extends Value> entry : m.entrySet()){
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        size = 1;
        table = new TableEntry[size];
    }

    @Override
    public Set<Key> keySet() {
        Set<Key> result = new HashSet<>(table.length);
        for (TableEntry<Key, Value> entry: table){
            if(!(entry == null || entry.isDeleted)) {
                result.add(entry.key);
            }
        }
        return result;
    }

    @Override
    public Collection<Value> values() {
        Collection<Value> result = new Vector<>(table.length);
        for (TableEntry<Key, Value> entry: table){
            if(!(entry == null || entry.isDeleted)) {
                result.add(entry.value);
            }
        }
        return result;
    }

    @Override
    public Set<Entry<Key, Value>> entrySet() {
        Set<Entry<Key, Value>> set = new HashSet<>(table.length);
        for(TableEntry<Key, Value> entry: table){
            if(!(entry == null || entry.isDeleted)) {
                set.add(new Entry<>() {
                    @Override
                    public Key getKey() {
                        return entry.key;
                    }

                    @Override
                    public Value getValue() {
                        return entry.value;
                    }

                    @Override
                    public Value setValue(Value value) {
                        return null;
                    }
                });
            }
        }
        return set;
    }


    private class TableEntry<Key, Value> {
        private final Key key;
        private final Value value;
        private boolean isDeleted = false;

        TableEntry(Key key, Value value) {
            this.key = key;
            this.value = value;
        }

        TableEntry(TableEntry<Key, Value> other){
            key = other.key;
            value = other.value;
            isDeleted = other.isDeleted;
        }
    }

    public HashTable(){
        size = 16;
        table = new TableEntry[size];
    }

    public HashTable(int size) {
        this.size = size;
        table = new TableEntry[size];
    }

    public HashTable(HashTable<Key, Value> other){
        size = other.size;
        table = new TableEntry[other.table.length];
        for(int i = 0; i < table.length; i++){
            if(other.table[i] != null) {
                table[i] = new TableEntry<>(other.table[i]);
            }
        }
    }

    @Override
    public Value put(Key key, Value value) {
        int id = findKey(key);
        if(id == -1){
            rehash();
            id = findKey(key);
        }
        Value old = (table[id] == null || table[id].isDeleted? null: table[id].value);
        table[id] = new TableEntry(key, value);
        return old;
    }

    @Override
    public Value get(Object key) {
        int idx = findKey((Key)key);

        if (idx == -1 || table[idx] == null || table[idx].isDeleted) {
            return null;
        }

        return table[idx].value;
    }

    private int findKey(Key key) {
        int hash = Math.abs(key.hashCode() % size);

        while(!(table[hash] == null || table[hash].key.equals(key))) {
            hash = (hash + 1) % size;

            if (hash == Math.abs(key.hashCode() % size)) {
                return -1;
            }
        }

        return hash;
    }

    private void rehash() {
        size *= 2;
        TableEntry<Key, Value>[] newTable = new TableEntry[size];
        TableEntry<Key, Value>[] old = table;
        table = newTable;
        for(int i = 0, id; i < size / 2; i++) {
            id = findKey(old[i].key);
            newTable[id] = old[i];
        }
    }

    @Override
    public String toString() {
        StringBuilder tableStringBuilder = new StringBuilder();

        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) {
                tableStringBuilder.append(i).append(": null");
            } else {
                tableStringBuilder.append(i).append(": key=").append(table[i].key).append(", value=").append(table[i].value);
            }

            if (i < table.length - 1) {
                tableStringBuilder.append("\n");
            }
        }

        return tableStringBuilder.toString();
    }
}