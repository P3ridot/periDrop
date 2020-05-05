package me.peridot.peridrop.user.rank;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import me.peridot.peridrop.user.User;

import java.util.Collections;
import java.util.NavigableSet;
import java.util.TreeSet;

public class RankSystem {

    protected NavigableSet<Rank> ranksSet = new TreeSet<>(Collections.reverseOrder());

    public void update(User user) {
        update(user.getRank());
    }

    public void update(Rank rank) {
        this.ranksSet.remove(rank);
        this.ranksSet.add(rank);

        ranksSet.forEach(rankFromSet -> {
            rankFromSet.setPosition(getPosition(rankFromSet));
        });
    }

    public void clear() {
        ranksSet.clear();
    }

    public NavigableSet<Rank> getRanksList() {
        return ranksSet;
    }

    public Rank getRank(int position) {
        if (position - 1 < this.ranksSet.size()) {
            return Iterables.get(this.ranksSet, position - 1);
        }
        return null;
    }

    public int getPosition(Rank rank) {
        return Iterables.indexOf(this.ranksSet, new Predicate<Rank>() {
            @Override
            public boolean apply(Rank input) {
                return rank.getUuid().equals(input.getUuid());
            }
        });
    }

}
