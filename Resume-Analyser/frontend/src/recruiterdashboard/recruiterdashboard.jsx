import { useContext, useEffect, useState } from "react";
import { usercontext } from "../appcontext";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import Styles from "./recruiterdashboard.module.css";

function RecruiterDashboard() {
    const navigate = useNavigate();
    const { serviceURL, role } = useContext(usercontext);
    const [companyName, setcompanyName] = useState("");
    const [title, settitle] = useState("");
    const [description, setdescription] = useState("");
    const [location, setlocation] = useState("");
    const [isloading, setisloading] = useState(false);
    const [jobs, setjobs] = useState([]);

    const loadJobs = () => {
        fetch(`${serviceURL}/recruiter/jobs`, { credentials: "include" })
            .then(response => response.ok ? response.json() : null)
            .then(data => {
                if (data != null) {
                    setjobs(data);
                }
            });
    };

    useEffect(() => {
        if (role && role !== "JOB_RECRUITER") {
            navigate("/");
            return;
        }
        loadJobs();
    }, [role]);

    const submitJob = () => {
        if (!companyName.trim() || !title.trim() || !description.trim() || !location.trim()) {
            toast.warn("Fill all job posting fields");
            return;
        }
        setisloading(true);
        fetch(`${serviceURL}/recruiter/jobs`, {
            method: "post",
            credentials: "include",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ companyName: companyName.trim(), title: title.trim(), description: description.trim(), location: location.trim() })
        }).then(response => {
            if (response.ok) {
                toast.success("Job posted successfully");
                setcompanyName("");
                settitle("");
                setdescription("");
                setlocation("");
                loadJobs();
            } else {
                toast.error("Failed to post job");
            }
            setisloading(false);
        }).catch(() => {
            toast.error("Network error");
            setisloading(false);
        });
    };

    return (
        <div className={Styles.container}>
            <div className={Styles.nav}>
                <h1>Recruiter Dashboard</h1>
                <button onClick={() => navigate("/")}>Home</button>
            </div>
            <div className={Styles.form}>
                <h2>Post New Job</h2>
                <input value={companyName} onChange={(event) => setcompanyName(event.target.value)} placeholder="Company Name" />
                <input value={title} onChange={(event) => settitle(event.target.value)} placeholder="Job Title" />
                <input value={location} onChange={(event) => setlocation(event.target.value)} placeholder="Location" />
                <textarea value={description} onChange={(event) => setdescription(event.target.value)} placeholder="Job Description" />
                <button onClick={submitJob} disabled={isloading}>{isloading ? "Posting..." : "Post Job"}</button>
            </div>

            <div className={Styles.jobs}>
                <h2>Your Job Posts</h2>
                {jobs.length === 0 ? <p>No jobs posted yet.</p> : jobs.map(job => (
                    <div key={job.id} className={Styles.card}>
                        <h3>{job.title}</h3>
                        <p><b>Company:</b> {job.companyName}</p>
                        <p><b>Location:</b> {job.location}</p>
                        <p>{job.description}</p>
                        <h4>Applicants ({job.applicants.length})</h4>
                        {job.applicants.length === 0 ? <p>No applications yet.</p> :
                            job.applicants.map((applicant, index) => (
                                <div key={index} className={Styles.applicant}>
                                    <p><b>Name:</b> {applicant.seekerName}</p>
                                    <p><b>Email:</b> {applicant.seekerEmail}</p>
                                    <p><b>Resume Score:</b> {applicant.resumeScore ?? "Not available"}</p>
                                    <p><b>ATS Score:</b> {applicant.atsScore ?? "Not available"}</p>
                                    <p><b>Analysed Role:</b> {applicant.analysedRole ?? "Not available"}</p>
                                </div>
                            ))
                        }
                    </div>
                ))}
            </div>
        </div>
    );
}

export default RecruiterDashboard;
