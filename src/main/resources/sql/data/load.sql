-- LoanLoan: {"loanId": "L001","applicantName": "Laumcing","loanAmount": 10000,"loanTerm": 12,"status": "APPROVED","interestRate": 7.5}
INSERT INTO loan (loan_id, applicant_name, loan_amount, loan_term, status, interest_rate, person_id)
VALUES ('L001', 'Laumcing', 10000.00, 12, 'APPROVED', 7.50, 'P001') ON CONFLICT (loan_id) DO NOTHING;

INSERT INTO loan (loan_id, applicant_name, loan_amount, loan_term, status, interest_rate, person_id)
VALUES ('L002', 'Zothanmawia', 15000.00, 24, 'PENDING', 6.50, 'P002') ON CONFLICT (loan_id) DO NOTHING;

INSERT INTO loan (loan_id, applicant_name, loan_amount, loan_term, status, interest_rate, person_id)
VALUES ('L003', 'Zothanmawia', 20000.00, 24, 'APPROVED', 6.50, 'P001') ON CONFLICT (loan_id) DO NOTHING;

INSERT INTO loan (loan_id, applicant_name, loan_amount, loan_term, status, interest_rate, person_id)
VALUES ('L004', 'Laumcing', 10000.00, 24, 'APPROVED', 6.50, 'P002') ON CONFLICT (loan_id) DO NOTHING;
