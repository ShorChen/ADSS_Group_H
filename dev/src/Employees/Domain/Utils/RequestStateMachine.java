package Employees.Domain.Utils;

import Employees.Shared.Enums.RequestStatus;

import java.util.*;

public class RequestStateMachine {
    private final Map<Configuration, State> delta = new HashMap<>();
    private final List<State> acceptedStates = new ArrayList<>();
    private final List<State> rejectedStates = new ArrayList<>();


    public RequestStateMachine() {
        State app = of(RequestStatus.ACCEPT, RequestStatus.PENDING, RequestStatus.PENDING);
        State arp = of(RequestStatus.ACCEPT, RequestStatus.REJECT, RequestStatus.PENDING);
        State aap = of(RequestStatus.ACCEPT, RequestStatus.ACCEPT, RequestStatus.PENDING);
        State rpp = of(RequestStatus.REJECT, RequestStatus.PENDING, RequestStatus.PENDING);
        State aar = of(RequestStatus.ACCEPT, RequestStatus.ACCEPT, RequestStatus.REJECT);
        State aaa = of(RequestStatus.ACCEPT, RequestStatus.ACCEPT, RequestStatus.ACCEPT);
        State rap = of(RequestStatus.REJECT, RequestStatus.ACCEPT, RequestStatus.PENDING);


        Letter nr = new Letter(Player.NEW_EMPLOYEE, RequestStatus.REJECT);
        Letter na = new Letter(Player.NEW_EMPLOYEE, RequestStatus.ACCEPT);
        Letter pr = new Letter(Player.PREVIOUS_EMPLOYEE, RequestStatus.REJECT);
        Letter mr = new Letter(Player.MANAGER, RequestStatus.REJECT);
        Letter ma = new Letter(Player.MANAGER, RequestStatus.ACCEPT);


        acceptedStates.add(aaa);
        rejectedStates.add(arp);
        rejectedStates.add(rpp);
        rejectedStates.add(rap);
        rejectedStates.add(aar);

        define(app, nr, arp);
        define(app, na, aap);
        define(app, pr, rpp);
        define(aap, mr, aar);
        define(aap, ma, aaa);
        define(aap, pr, rap);
    }

    private State of(RequestStatus first, RequestStatus second, RequestStatus third) {
        return new State(first, second, third);
    }

    private void define(State p, Letter w, State q) {
        delta.put(new Configuration(p, w), q);
    }

    public State apply(State p, Letter w) {
        State q = delta.get(new Configuration(p, w));
        return q == null ? p : q;
    }

    public boolean isAccepted(State p) {
        return acceptedStates.contains(p);
    }

    public boolean isRejected(State p) {
        return rejectedStates.contains(p);
    }

    public enum Player {
        PREVIOUS_EMPLOYEE, NEW_EMPLOYEE, MANAGER
    }

    public record Letter(Player id, RequestStatus status) {
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Letter(Player id1, RequestStatus status1))) return false;
            return status == status1 && Objects.equals(id, id1);
        }
    }

    public record State(RequestStatus first, RequestStatus second, RequestStatus third) {

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof State(RequestStatus first1, RequestStatus second1, RequestStatus third1))) return false;
            return first == first1 && second == second1 && third == third1;
        }

    }

    public record Configuration(State state, Letter letter) {

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Configuration(State state1, Letter letter1))) return false;
            return Objects.equals(state, state1) && Objects.equals(letter, letter1);
        }

    }
}
