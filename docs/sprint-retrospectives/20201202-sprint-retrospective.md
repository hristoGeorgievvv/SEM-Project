# Sprint Retrospective, Iteration #2

Project: Student-house food management

Group: op27-sem54

| User story | Task                                | Assinged to | Estimated Effort  per Task | Actual Effortper Task | Done | Notes |
| ---------- | ----------------------------------- | ----------- | -------------------------- | --------------------- | ---- | ----- |
| #18        | Logic for processing credits        | Robert      | 4h                         | ~5h                   | ✔️   |       |
| #22        | Adapter beween reddis json and main | Robert      | 3h                         | ~3h                   | ✔️   |       |
| #2         | Product expiration date             | Robert, Lyuben | Combined effort of #22, #20 | >10h              | ✔️   | Design, implementation and setup|
| #20        | Fridge sends events to main         | Lyuben      | 4h                         | ~5h                   | ✔️   |       |
| #5         | User gets flagged with credits < -50| Luc         | 1h                         | ~4h                   | ✔️   |       |
| #1         | Reset functionality                 | Luc         | 30m                        | ~1h30m                | ✔️   |       |
| #19        | Main microservice security          | Maarten     | 5h                         | ~4h30m                | ✔️   |       |
| #21        | Link main microservice to auth microservice| Luc, Maarten| 2h                         | ~4h30m                | ✔️   |       |
| #24        | Undo feature                        | Hristo | ~5h                              | ~6h                | ✔️   |       |
| #25       | Security to fridge-service           |  Hristo | 45m                         | ~45m              | ✔️   |       |
| #9       | Fridge: User-portion map, methods for expired, finished, available products, transaction validation|  Anton | 2h   | ~2h30, | ✔️   |       |
| #9       | Fridge: Expand test suite |  Anton | 2h                         | ~2h | ✔️   |  100% test coverage for entities Product and Fridge + other tests     |
| #9       | Fridge: Finish up entities & other small changes |  Anton | 30m                         | ~1h | ✔️   |       |

## Main Problems Encountered

### Compatibility issues

For running the program where particular libraries and configs needed.
For running tests, there were different libraries and configs needed.
These were not always easy to combine or not compatible.
We should have discussed more on what libraries we used for what
and how those things would come together so that we won't get stuck on issues like that in the future.

## Adjustments for the next Sprint Plan