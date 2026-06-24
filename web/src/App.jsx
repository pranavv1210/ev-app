import { useEffect } from 'react';

export default function App() {
  useEffect(() => {
        function animateCounter(id, start, end, duration) {
            const obj = document.getElementById(id);
            if(!obj) return;
            let startTimestamp = null;
            const step = (timestamp) => {
                if (!startTimestamp) startTimestamp = timestamp;
                const progress = Math.min((timestamp - startTimestamp) / duration, 1);
                obj.innerText = Math.floor(progress * (end - start) + start).toLocaleString();
                if (progress < 1) {
                    window.requestAnimationFrame(step);
                }
            };
            window.requestAnimationFrame(step);
        }

        const observer = new IntersectionObserver((entries) => {
            entries.forEach((entry) => {
                if (entry.isIntersecting) {
                    animateCounter("carbon-saved", 0, 12850, 2000);
                    animateCounter("rides-completed", 0, 84200, 2000);
                    animateCounter("trees-equivalent", 0, 540, 2000);
                    observer.unobserve(entry.target);
                }
            });
        }, { threshold: 0.5 });

        const impactSection = document.getElementById("carbon-saved")?.parentElement?.parentElement;
        if (impactSection) observer.observe(impactSection);

        const revealElements = document.querySelectorAll('.glass-panel');
        const revealObserver = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('opacity-100', 'translate-y-0');
                    entry.target.classList.remove('opacity-0', 'translate-y-10');
                }
            });
        }, { threshold: 0.1 });

        revealElements.forEach(el => {
            el.classList.add('opacity-0', 'translate-y-10', 'transition-all', 'duration-700');
            revealObserver.observe(el);
        });

        const accordions = document.querySelectorAll('.faq-btn');
        accordions.forEach(btn => {
            btn.onclick = () => btn.nextElementSibling.classList.toggle('hidden');
        });
  }, []);

  return (
    <div className="font-body-md antialiased text-white bg-background min-h-screen">
      

<nav className="fixed top-0 w-full z-50 bg-background/70 backdrop-blur-xl border-b border-glass-border">
<div className="max-w-container-max mx-auto px-margin-mobile md:px-gutter py-4 flex justify-between items-center">
<div className="font-headline-md text-headline-md font-bold text-primary tracking-tighter">VoltRide</div>
<div className="hidden md:flex items-center gap-stack-lg">
<a className="font-body-md text-on-surface-variant hover:text-primary transition-colors duration-300" href="#">Models</a>
<a className="font-body-md text-on-surface-variant hover:text-primary transition-colors duration-300" href="#">Why VoltRide</a>
<a className="font-body-md text-on-surface-variant hover:text-primary transition-colors duration-300" href="#">Impact</a>
<a className="font-body-md text-primary border-b-2 border-primary pb-1" href="#">Waitlist</a>
</div>
<a href="https://docs.google.com/forms/d/e/1FAIpQLScU85kgbPmiIvhF3nuTBkpgXawRpTDbzRYWVcJZ0KvLFLEEcg/viewform?usp=publish-editor" target="_blank" rel="noopener noreferrer" className="px-6 py-2 bg-primary-container text-on-primary font-bold rounded-full electric-glow transition-transform active:scale-95">
                Join Waitlist
            </a>
</div>
</nav>

<section className="relative min-h-screen flex items-center pt-24 overflow-hidden">

<div className="relative z-10 max-w-container-max mx-auto px-margin-mobile md:px-gutter w-full">
<div className="max-w-3xl">
<h1 className="font-display-lg-mobile md:font-display-lg text-display-lg-mobile md:text-display-lg text-primary mb-6">
                    The Future of Bengaluru Mobility is <span className="gradient-text">Electric.</span>
</h1>
<p className="font-body-lg text-body-lg text-text-secondary mb-10 max-w-xl">
                    Cleaner rides. Safer journeys. A premium experience built entirely around high-performance electric vehicles.
                </p>
<div className="flex flex-col sm:flex-row gap-stack-md">
<a href="https://docs.google.com/forms/d/e/1FAIpQLScU85kgbPmiIvhF3nuTBkpgXawRpTDbzRYWVcJZ0KvLFLEEcg/viewform?usp=publish-editor" target="_blank" rel="noopener noreferrer" className="inline-block px-8 py-4 bg-primary-container text-on-primary font-bold rounded-full electric-glow text-lg transition-all hover:scale-105 active:scale-95">
                        Join the Waitlist
                    </a>
<button className="px-8 py-4 bg-transparent border border-glass-border text-primary font-bold rounded-full hover:border-primary-container transition-all text-lg">
                        Explore the Vision
                    </button>
</div>
</div>
</div>

<div className="absolute right-0 top-1/2 -translate-y-1/2 hidden xl:block w-1/3">
<div className="glass-panel p-6 rounded-2xl mb-6 -rotate-6 animate-float translate-x-12">
<div className="flex items-center gap-4">
<div className="w-12 h-12 rounded-full bg-primary-container/20 flex items-center justify-center">
<span className="material-symbols-outlined text-primary-container">electric_car</span>
</div>
<div>
<div className="text-primary font-bold">EV Cab</div>
<div className="text-text-secondary text-sm">Luxury Sedan Experience</div>
</div>
</div>
</div>
<div className="glass-panel p-6 rounded-2xl mb-6 rotate-3 animate-float [animation-delay:1s]">
<div className="flex items-center gap-4">
<div className="w-12 h-12 rounded-full bg-secondary-fixed/20 flex items-center justify-center">
<span className="material-symbols-outlined text-secondary-fixed">two_wheeler</span>
</div>
<div>
<div className="text-primary font-bold">EV Bike</div>
<div className="text-text-secondary text-sm">Swift Urban Agility</div>
</div>
</div>
</div>
<div className="glass-panel p-6 rounded-2xl -rotate-2 animate-float [animation-delay:2s] translate-x-8">
<div className="flex items-center gap-4">
<div className="w-12 h-12 rounded-full bg-tertiary-fixed/20 flex items-center justify-center">
<span className="material-symbols-outlined text-tertiary-fixed">electric_rickshaw</span>
</div>
<div>
<div className="text-primary font-bold">EV Auto</div>
<div className="text-text-secondary text-sm">Smart Commute</div>
</div>
</div>
</div>
</div>
</section>

<section className="py-section-gap">
<div className="max-w-container-max mx-auto px-margin-mobile md:px-gutter">
<div className="text-center mb-20">
<span className="text-primary-container font-bold tracking-widest text-xs uppercase">The Paradigm Shift</span>
<h2 className="font-headline-xl text-headline-xl text-primary mt-4">Sustainability as the Default.</h2>
</div>
<div className="grid md:grid-cols-2 gap-stack-lg items-stretch">

<div className="glass-panel rounded-3xl p-10 border border-white/5 bg-surface-container-lowest/50 group">
<div className="text-error font-bold mb-4 flex items-center gap-2">
<span className="material-symbols-outlined">warning</span> Legacy Platforms
                    </div>
<h3 className="font-headline-md text-headline-md text-primary mb-6">Sustainability as an Option</h3>
<p className="text-text-secondary mb-8">Current ride platforms treat green energy as a niche add-on. You pay extra for low-impact travel, while the default contributes to urban smog.</p>
<div className="relative rounded-2xl overflow-hidden aspect-video bg-surface-container">
<div className="absolute inset-0 bg-gradient-to-t from-background/80 to-transparent"></div>
<img className="w-full h-full object-cover opacity-50 grayscale group-hover:grayscale-0 transition-all duration-700" data-alt="A gritty, cinematic long exposure photo of a congested Bengaluru street at dusk with tail lights blurring into a red smoggy haze. The atmosphere feels heavy, industrial, and chaotic, emphasizing the carbon footprint of traditional internal combustion engine vehicles in a dense urban environment." src="https://lh3.googleusercontent.com/aida-public/AB6AXuAowuQq6laBShtRUrrUdWhMXUctEXUC_3REkbyqEVicXnsi428BDb41L8elBNP0r7pf_oT_zH2It1L_Y8n7wucUDxtQABwgLPILYLMY1eUQ9f4dHLDAGAgTL5pe_v7_n61ne6_z56zeQggXvj1KUVEmcixwZPXDrQAFhg4qbXYjZILr3gOHfzG3nJ8k2KQXsxl8kQfxd3IKh53ayxfvmZ09llWtbx528Fl3ugS7UZZZ9WlCarBbSOqYVqrg7E74frxs_6o_h7cUOBHX"/>
</div>
</div>

<div className="glass-panel rounded-3xl p-10 border border-primary-container/20 bg-primary-container/5 group">
<div className="text-primary-container font-bold mb-4 flex items-center gap-2">
<span className="material-symbols-outlined" style={{"fontVariationSettings":"'FILL' 1"}}>eco</span> VoltRide Mobility
                    </div>
<h3 className="font-headline-md text-headline-md text-primary mb-6">Sustainability as the Standard</h3>
<p className="text-text-secondary mb-8">Every single vehicle in our fleet is 100% electric. No exceptions. No compromise. Premium travel that repairs the city's air with every kilometer.</p>
<div className="relative rounded-2xl overflow-hidden aspect-video bg-surface-container">
<div className="absolute inset-0 bg-gradient-to-t from-primary-container/20 to-transparent"></div>
<img className="w-full h-full object-cover group-hover:scale-110 transition-all duration-700" data-alt="A high-end, futuristic digital render of a sleek white electric car driving through a lush, clean, hyper-modern tech park in Bengaluru. The lighting is soft and cinematic, with electric green light trails highlighting the car's path. The atmosphere is quiet, serene, and ultra-clean." src="https://lh3.googleusercontent.com/aida-public/AB6AXuDiEUe9-rcjyAvLai-t550uGpXpOsPepsPJsvf5yGphR3k-QCXfZHFV68znTGHZgxATFwUTScSmV7D5ZPyN_-Fk5EdvkHcIQoH2uXq7c4KC5GYJbho2fNLWvGUPiJ25GapldNS4w3gxI8g6gyKVU_hJf87UaxOl2es56JPLrBNg5wE5whzyVX7zbN2shvYACCCpkrGnlMGy9dPMxNXUXe8TeoMAqJy6ZQyXyqcpQgKnxF5oXZgDy3019D4TGU9NNPv4sl4cF2qPNN9Q"/>
</div>
</div>
</div>
</div>
</section>

<section className="py-section-gap bg-surface-container-lowest relative overflow-hidden">
<div className="absolute top-0 right-0 w-full h-full opacity-10">
<svg className="w-full h-full" fill="none" viewBox="0 0 800 800" xmlns="http://www.w3.org/2000/svg">
<path d="M100 100L200 150L150 300L400 350L600 200L700 400L500 600L300 700L100 550Z" stroke="#00FF66" strokeWidth="2"></path>
<circle cx="200" cy="150" fill="#00FF66" r="10"></circle>
<circle cx="600" cy="200" fill="#00FF66" r="10"></circle>
<circle cx="300" cy="700" fill="#00FF66" r="10"></circle>
</svg>
</div>
<div className="max-w-container-max mx-auto px-margin-mobile md:px-gutter relative z-10 grid md:grid-cols-2 gap-stack-lg items-center">
<div>
<span className="text-primary-container font-bold tracking-widest text-xs uppercase">Engineered for Bengaluru</span>
<h2 className="font-headline-xl text-headline-xl text-primary mt-4 mb-6">Built for the Silicon Valley of India.</h2>
<p className="text-body-lg text-text-secondary mb-8">In the heart of innovation, mobility shouldn't be archaic. VoltRide leverages Bengaluru’s tech spirit to create a transportation network that’s as smart as its residents.</p>
<div className="space-y-4">
<div className="flex items-center gap-4 p-4 glass-panel rounded-xl">
<span className="material-symbols-outlined text-primary-container">location_on</span>
<span className="text-primary font-medium">Smart Route Optimization for Bengaluru Traffic</span>
</div>
<div className="flex items-center gap-4 p-4 glass-panel rounded-xl">
<span className="material-symbols-outlined text-primary-container">bolt</span>
<span className="text-primary font-medium">Ultra-Fast Charging Hub Network</span>
</div>
</div>
</div>
<div className="relative">
<div className="aspect-square rounded-3xl overflow-hidden glass-panel p-2">
<div className="w-full h-full rounded-2xl overflow-hidden relative">
<div className="bg-cover bg-center w-full h-full grayscale opacity-60" data-location="Bengaluru" style={{"backgroundImage":"url('https"}}></div>
<div className="absolute inset-0 bg-gradient-to-br from-primary-container/20 via-transparent to-background/80"></div>
<div className="absolute inset-0 flex items-center justify-center">
<div className="w-32 h-32 bg-primary-container/10 rounded-full flex items-center justify-center animate-pulse">
<div className="w-16 h-16 bg-primary-container/20 rounded-full flex items-center justify-center">
<div className="w-4 h-4 bg-primary-container rounded-full shadow-[0_0_20px_#00FF66]"></div>
</div>
</div>
</div>
</div>
</div>
</div>
</div>
</section>

<section className="py-section-gap">
<div className="max-w-container-max mx-auto px-margin-mobile md:px-gutter">
<div className="text-center mb-16">
<h2 className="font-headline-xl text-headline-xl text-primary">Redefining the Premium Ride.</h2>
</div>
<div className="grid grid-cols-1 md:grid-cols-3 gap-6">

<div className="glass-panel p-8 rounded-3xl rim-light transition-all hover:-translate-y-2 hover:bg-surface-variant/20">
<span className="material-symbols-outlined text-4xl text-primary-container mb-6">battery_charging_full</span>
<h3 className="font-headline-md text-headline-md text-primary mb-3">100% Electric</h3>
<p className="text-text-secondary">Every single vehicle is a zero-emission EV, meticulously maintained for peak performance.</p>
</div>

<div className="glass-panel p-8 rounded-3xl rim-light transition-all hover:-translate-y-2 hover:bg-surface-variant/20 md:col-span-2 bg-gradient-to-br from-surface-charcoal to-on-primary-container/10">
<div className="flex flex-col md:flex-row gap-8 items-center">
<div className="flex-1">
<span className="material-symbols-outlined text-4xl text-secondary mb-6">female</span>
<h3 className="font-headline-md text-headline-md text-primary mb-3">Women Mode</h3>
<p className="text-text-secondary">Safety isn't a feature; it's a foundation. Verified female drivers matched with female passengers for absolute peace of mind.</p>
</div>
<div className="w-full md:w-1/3 aspect-video md:aspect-square bg-background/50 rounded-2xl flex items-center justify-center border border-glass-border">
<span className="material-symbols-outlined text-6xl text-secondary animate-pulse" style={{"fontVariationSettings":"'FILL' 1"}}>security</span>
</div>
</div>
</div>

<div className="glass-panel p-8 rounded-3xl rim-light transition-all hover:-translate-y-2 hover:bg-surface-variant/20">
<span className="material-symbols-outlined text-4xl text-warning-amber mb-6">monitoring</span>
<h3 className="font-headline-md text-headline-md text-primary mb-3">Carbon Tracking</h3>
<p className="text-text-secondary">Visualize your environmental contribution with real-time carbon offset metrics after every trip.</p>
</div>

<div className="glass-panel p-8 rounded-3xl rim-light transition-all hover:-translate-y-2 hover:bg-surface-variant/20">
<span className="material-symbols-outlined text-4xl text-tertiary-fixed-dim mb-6">workspace_premium</span>
<h3 className="font-headline-md text-headline-md text-primary mb-3">Premium Experience</h3>
<p className="text-text-secondary">Silence, cleanliness, and comfort. Our interiors are designed as mobile tech sanctuaries.</p>
</div>

<div className="glass-panel p-8 rounded-3xl rim-light transition-all hover:-translate-y-2 hover:bg-surface-variant/20">
<span className="material-symbols-outlined text-4xl text-primary-container mb-6">verified_user</span>
<h3 className="font-headline-md text-headline-md text-primary mb-3">Verified Drivers</h3>
<p className="text-text-secondary">Rigorous vetting and training processes to ensure the highest standards of urban conduct.</p>
</div>
</div>
</div>
</section>

<section className="py-section-gap relative overflow-hidden bg-surface-charcoal/30">
<div className="max-w-container-max mx-auto px-margin-mobile md:px-gutter">
<div className="flex flex-col lg:flex-row items-center gap-stack-lg">
<div className="w-full lg:w-1/2">
<div className="relative max-w-sm mx-auto">

<div className="glass-panel aspect-[9/19] rounded-[3rem] border-8 border-background overflow-hidden relative shadow-2xl">
<div className="absolute top-0 left-1/2 -translate-x-1/2 w-1/3 h-6 bg-background rounded-b-2xl z-20"></div>
<div className="p-6 pt-12 h-full flex flex-col">
<div className="flex justify-between items-center mb-8">
<span className="font-bold text-primary">VoltRide</span>
<span className="material-symbols-outlined text-secondary">notifications</span>
</div>
<div className="bg-secondary-container/20 border border-secondary/30 p-4 rounded-2xl mb-6">
<div className="text-xs text-secondary-fixed mb-1 uppercase font-bold tracking-tighter">Safety Mode Active</div>
<div className="text-primary font-bold">Women-Only Matching</div>
</div>
<div className="flex-grow flex flex-col justify-center items-center gap-6">
<div className="w-24 h-24 rounded-full border-4 border-secondary p-1">
<img className="w-full h-full object-cover rounded-full" data-alt="A professional headshot of a friendly Indian female driver in her late 20s, wearing a clean white polo shirt. She is smiling confidently. The background is a soft-focus interior of a modern electric car." src="https://lh3.googleusercontent.com/aida-public/AB6AXuBzEd9yuzT94TfyF1vm5zfcbnmsKHMmMoUSs9ZDqNy_sEd-1pf6aY0i9DNWfdLDMDyiZFrOUvuuEriMFZjS-cclC70pk6CN8DiIGJeu2hQGQ28nkqoZTBcBSD7m2fZ8u1OWL5qjNeZ8RsYD6GPV6ZRmQnyVjHXtEP386lQnQ_uD12UdYyTYebVoFHPeV60WE8FUjGEIUFv4GoR_R5s5Amo7iDGWUuAJ4Hmio8V4GKu7r02doUersHz8NHF8tUGlZBcM3K9bknST8YPs"/>
</div>
<div className="text-center">
<div className="text-primary text-xl font-bold">Aarti Sharma</div>
<div className="text-text-secondary text-sm">Top Rated • 4.98 ★</div>
</div>
<button className="w-full py-4 bg-error text-on-error font-extrabold rounded-2xl flex items-center justify-center gap-2">
<span className="material-symbols-outlined" style={{"fontVariationSettings":"'FILL' 1"}}>sos</span> SOS EMERGENCY
                                    </button>
</div>
<div className="mt-auto pt-6 border-t border-glass-border">
<div className="flex justify-between text-xs text-text-secondary">
<span>Trip Sharing Active</span>
<span className="text-secondary font-bold">LIVE</span>
</div>
</div>
</div>
</div>

<div className="absolute -right-12 top-1/4 glass-panel p-4 rounded-xl border-secondary/20 shadow-xl animate-float">
<div className="flex items-center gap-3">
<div className="w-2 h-2 rounded-full bg-secondary"></div>
<span className="text-sm font-medium">Verified Female Partner</span>
</div>
</div>
</div>
</div>
<div className="w-full lg:w-1/2">
<span className="text-secondary font-bold tracking-widest text-xs uppercase">Safety Redefined</span>
<h2 className="font-headline-xl text-headline-xl text-primary mt-4 mb-6">Women Mode: Designed for Absolute Peace.</h2>
<p className="text-body-lg text-text-secondary mb-8">
                        Select "Women Mode" to be exclusively matched with our verified female driver-partners. VoltRide is the first platform to prioritize this level of social safety at its core, not as an afterthought.
                    </p>
<ul className="space-y-4">
<li className="flex items-start gap-4">
<span className="material-symbols-outlined text-secondary bg-secondary/10 p-2 rounded-lg">verified</span>
<div>
<h4 className="text-primary font-bold">Tiered Verification</h4>
<p className="text-text-secondary text-sm">Background checks that exceed industry standards for every partner.</p>
</div>
</li>
<li className="flex items-start gap-4">
<span className="material-symbols-outlined text-secondary bg-secondary/10 p-2 rounded-lg">share_location</span>
<div>
<h4 className="text-primary font-bold">Continuous Monitoring</h4>
<p className="text-text-secondary text-sm">Real-time trip sharing with emergency contacts and our 24/7 central command.</p>
</div>
</li>
</ul>
</div>
</div>
</div>
</section>

<section className="py-section-gap relative overflow-hidden">

<div className="max-w-container-max mx-auto px-margin-mobile md:px-gutter relative z-10">
<div className="glass-panel p-12 rounded-[3rem] border-primary-container/20 text-center">
<h2 className="font-headline-xl text-headline-xl text-primary mb-12">Your Contribution, Quantified.</h2>
<div className="grid grid-cols-1 md:grid-cols-3 gap-stack-lg">
<div className="p-8">
<div className="text-5xl md:text-7xl font-extrabold text-primary-container mb-2 tracking-tighter" id="carbon-saved">0</div>
<div className="text-text-secondary font-bold uppercase tracking-widest text-sm">Kg CO₂ Saved</div>
</div>
<div className="p-8">
<div className="text-5xl md:text-7xl font-extrabold text-primary mb-2 tracking-tighter" id="rides-completed">0</div>
<div className="text-text-secondary font-bold uppercase tracking-widest text-sm">Cleaner Rides Today</div>
</div>
<div className="p-8">
<div className="text-5xl md:text-7xl font-extrabold text-secondary-fixed-dim mb-2 tracking-tighter" id="trees-equivalent">0</div>
<div className="text-text-secondary font-bold uppercase tracking-widest text-sm">Tree Equivalents</div>
</div>
</div>
</div>
</div>
</section>

<section className="py-section-gap">
<div className="max-w-container-max mx-auto px-margin-mobile md:px-gutter">
<div className="text-center mb-16">
<h2 className="font-headline-xl text-headline-xl text-primary">Simple. Sustainable. Seamless.</h2>
</div>
<div className="relative">

<div className="absolute left-1/2 top-0 bottom-0 w-px bg-gradient-to-b from-primary-container via-primary-container/50 to-transparent hidden md:block"></div>
<div className="space-y-24">

<div className="flex flex-col md:flex-row items-center gap-12 relative">
<div className="w-full md:w-1/2 md:text-right">
<h3 className="font-headline-md text-headline-md text-primary mb-4">1. Open the App</h3>
<p className="text-text-secondary">Available soon on iOS and Android. A minimalist interface designed for speed and clarity.</p>
</div>
<div className="absolute left-1/2 -translate-x-1/2 w-12 h-12 bg-background border-4 border-primary-container rounded-full hidden md:flex items-center justify-center z-10">
<span className="text-primary-container font-bold">1</span>
</div>
<div className="w-full md:w-1/2">
<div className="glass-panel p-4 rounded-2xl aspect-video bg-surface-container-high/40 border-glass-border">
<div className="w-full h-full rounded-xl bg-background/50 flex items-center justify-center">
<span className="material-symbols-outlined text-6xl text-primary-container/40">app_shortcut</span>
</div>
</div>
</div>
</div>

<div className="flex flex-col md:flex-row-reverse items-center gap-12 relative">
<div className="w-full md:w-1/2 md:text-left">
<h3 className="font-headline-md text-headline-md text-primary mb-4">2. Choose Your EV</h3>
<p className="text-text-secondary">From quick city zips on an EV Bike to premium boardroom arrivals in an EV Cab.</p>
</div>
<div className="absolute left-1/2 -translate-x-1/2 w-12 h-12 bg-background border-4 border-primary-container rounded-full hidden md:flex items-center justify-center z-10">
<span className="text-primary-container font-bold">2</span>
</div>
<div className="w-full md:w-1/2">
<div className="glass-panel p-4 rounded-2xl aspect-video bg-surface-container-high/40 border-glass-border">
<div className="w-full h-full rounded-xl bg-background/50 flex items-center justify-center">
<span className="material-symbols-outlined text-6xl text-primary-container/40">electric_car</span>
</div>
</div>
</div>
</div>

<div className="flex flex-col md:flex-row items-center gap-12 relative">
<div className="w-full md:w-1/2 md:text-right">
<h3 className="font-headline-md text-headline-md text-primary mb-4">3. Ride Sustainably</h3>
<p className="text-text-secondary">Silent, smooth, and powerful. Enjoy the unique serenity of an electric-only transport experience.</p>
</div>
<div className="absolute left-1/2 -translate-x-1/2 w-12 h-12 bg-background border-4 border-primary-container rounded-full hidden md:flex items-center justify-center z-10">
<span className="text-primary-container font-bold">3</span>
</div>
<div className="w-full md:w-1/2">
<div className="glass-panel p-4 rounded-2xl aspect-video bg-surface-container-high/40 border-glass-border">
<div className="w-full h-full rounded-xl bg-background/50 flex items-center justify-center">
<span className="material-symbols-outlined text-6xl text-primary-container/40">bolt</span>
</div>
</div>
</div>
</div>
</div>
</div>
</div>
</section>

<section className="py-section-gap bg-surface-container-lowest">
<div className="max-w-container-max mx-auto px-margin-mobile md:px-gutter">
<h2 className="font-headline-xl text-headline-xl text-primary mb-16 text-center">Engineered for Every Journey.</h2>
<div className="flex overflow-x-auto pb-12 gap-8 snap-x no-scrollbar">

<div className="min-w-[300px] md:min-w-[450px] snap-center glass-panel rounded-3xl overflow-hidden flex flex-col hover:border-primary-container/30 transition-all group">
<div className="h-64 relative overflow-hidden bg-surface-container">
<img className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-700" data-alt="A sleek, high-end electric motorcycle in matte black with electric green neon accents, parked in a minimalist showroom with concrete floors and soft overhead lighting. The design is aerodynamic and futuristic, emphasizing urban performance." src="https://lh3.googleusercontent.com/aida-public/AB6AXuAGo3IvRNMcTEcVBO-pgvMdTXQuA_47WPEsJwxbCvPQkhVRjOHHdz7W_6QhoblTkUS9ntrbJPmc0S6c1MB0Gg_Hr0P_j2M5BT-Ho9Ic17fyXTtWyo5JmwUKkxScgjZ24SEtuFJOG4f7OStxQvNvlhuM6dQIwJHCB-dHOq7wtAo6WNCObERnE-xfT1XDQgezi37szOFTPJakm2bUsBPIbAk6I-YYinJXmh4LyTU4r2pfpg7N3ZR4BDCZVSnwGY89E8ro0u9Aveg7A_Qk"/>
<div className="absolute top-4 right-4 bg-primary-container text-on-primary font-bold px-3 py-1 rounded-full text-xs">SWIFT</div>
</div>
<div className="p-8">
<h3 className="font-headline-md text-headline-md text-primary mb-2">EV Bike</h3>
<p className="text-text-secondary mb-6">Master Bengaluru's traffic with nimble, high-torque electric bikes.</p>
<div className="flex justify-between border-t border-glass-border pt-6">
<div className="text-center">
<div className="text-primary font-bold">120km</div>
<div className="text-[10px] text-text-secondary uppercase">Range</div>
</div>
<div className="text-center">
<div className="text-primary font-bold">4.2s</div>
<div className="text-[10px] text-text-secondary uppercase">0-60 km/h</div>
</div>
<div className="text-center">
<div className="text-primary font-bold">Premium</div>
<div className="text-[10px] text-text-secondary uppercase">Tier</div>
</div>
</div>
</div>
</div>

<div className="min-w-[300px] md:min-w-[450px] snap-center glass-panel rounded-3xl overflow-hidden flex flex-col border-primary-container/40 bg-primary-container/5 hover:border-primary-container/60 transition-all group">
<div className="h-64 relative overflow-hidden bg-surface-container">
<img className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-700" data-alt="A luxurious, minimalist white electric sedan with tinted panoramic glass roof, driving on a clean urban road at golden hour. The car's silhouette is smooth and flowing, representing a top-tier premium transportation experience. The lighting is warm and expensive." src="https://lh3.googleusercontent.com/aida-public/AB6AXuBk3qdH-g7mqxs9RlC_DGM-Q2yIYUCE2AhRnDElHC1lzceZwww-eKRte9ubg6h41WzIMlr9OFG0wlMBHhfR66u_ABdfvg7qSoADE-_uIPdmgnkmT4CGlqOnfjVgpcqx5sbuxtH360UIZvSs6VGdvOKE9zB1Wk17kikwr9ehEqwvKHPccSN8mAUlTCbjuG9OVmbkIsB6yFHuOoWyxT3hyRORomgS_bRkmd_X3qcvJZd7pG-x3Llb8LFcIcZWVB5CBTpA2XY-mi4xTde0"/>
<div className="absolute top-4 right-4 bg-primary-container text-on-primary font-bold px-3 py-1 rounded-full text-xs">LUXURY</div>
</div>
<div className="p-8">
<h3 className="font-headline-md text-headline-md text-primary mb-2">EV Cab</h3>
<p className="text-text-secondary mb-6">The gold standard of urban mobility. Silent cabins and premium comfort.</p>
<div className="flex justify-between border-t border-glass-border pt-6">
<div className="text-center">
<div className="text-primary font-bold">450km</div>
<div className="text-[10px] text-text-secondary uppercase">Range</div>
</div>
<div className="text-center">
<div className="text-primary font-bold">ADAS</div>
<div className="text-[10px] text-text-secondary uppercase">Safety</div>
</div>
<div className="text-center">
<div className="text-primary font-bold">Elite</div>
<div className="text-[10px] text-text-secondary uppercase">Tier</div>
</div>
</div>
</div>
</div>

<div className="min-w-[300px] md:min-w-[450px] snap-center glass-panel rounded-3xl overflow-hidden flex flex-col hover:border-primary-container/30 transition-all group">
<div className="h-64 relative overflow-hidden bg-surface-container">
<img className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-700" data-alt="A modern, redesigned electric auto-rickshaw with a metallic silver body and bright green detailing. It looks robust yet sophisticated, featuring LED headlights and a digital display for the driver. The setting is a clean street in an innovative tech hub." src="https://lh3.googleusercontent.com/aida-public/AB6AXuCvkG4PN51kxrHtC2xiHZINplvQUMfusf1ZhNn4m9-wpwevkZpkUARNdMN9wDjYS7KtR_ArZRmEZ2nUaxKzP6inukVrDNLvFmhDsnSQSGqO78Se0yJi_ruEEA3hEbenOogSLKHhBnoLvuQ6FGh2DFWF-XKAX6-fEWh-wrw_dAIw27cDCsxPlocdqdP8ZjFKjxEG201RyoURAAvMFZuuUjH28jKnjt43RaQI8NKfLUr7QlN-zwjkus8IkvkvzMyUYmrz51NnsYAz_HWd"/>
<div className="absolute top-4 right-4 bg-primary-container text-on-primary font-bold px-3 py-1 rounded-full text-xs">VERSATILE</div>
</div>
<div className="p-8">
<h3 className="font-headline-md text-headline-md text-primary mb-2">EV Auto</h3>
<p className="text-text-secondary mb-6">Reimagining the icon of Indian cities for a sustainable tomorrow.</p>
<div className="flex justify-between border-t border-glass-border pt-6">
<div className="text-center">
<div className="text-primary font-bold">180km</div>
<div className="text-[10px] text-text-secondary uppercase">Range</div>
</div>
<div className="text-center">
<div className="text-primary font-bold">Smart</div>
<div className="text-[10px] text-text-secondary uppercase">Digital</div>
</div>
<div className="text-center">
<div className="text-primary font-bold">Urban</div>
<div className="text-[10px] text-text-secondary uppercase">Tier</div>
</div>
</div>
</div>
</div>
</div>
</div>
</section>

<section className="py-section-gap">
<div className="max-w-4xl mx-auto px-margin-mobile text-center">
<h2 className="font-display-lg-mobile md:text-5xl text-primary font-light italic mb-12">"The future of cities isn't just about faster movement, but about moving better."</h2>
<div className="space-y-6 text-xl text-text-secondary leading-relaxed">
<p>We envision a Bengaluru where the hum of engines is replaced by the silent whisper of electricity. A city where the air is as clear as our vision for safer, more dignified transport for every citizen.</p>
<p>VoltRide isn't just a ride-sharing app. It's a statement. A commitment to the planet and a promise to the passengers who demand more from their daily commute.</p>
</div>
</div>
</section>

<section id="waitlist" className="py-section-gap relative">
<div className="max-w-container-max mx-auto px-margin-mobile md:px-gutter">
<div className="glass-panel p-12 md:p-24 rounded-[4rem] text-center relative overflow-hidden border border-primary-container/30">
<div className="absolute inset-0 bg-gradient-to-br from-primary-container/10 via-transparent to-primary-container/5"></div>
<div className="relative z-10 max-w-3xl mx-auto flex flex-col items-center">
<div className="mb-6 inline-block bg-primary-container/10 border border-primary-container/30 rounded-full px-6 py-2 text-primary-container font-bold tracking-widest text-sm uppercase">
  Launching First in Bengaluru, Karnataka 🇮🇳
</div>
<h2 className="font-headline-xl text-headline-xl text-primary mb-6">Help shape the future of sustainable mobility.</h2>
<p className="text-body-lg text-text-secondary mb-12">Join the early access list today.</p>
<div className="flex flex-col sm:flex-row gap-4 w-full justify-center">
<a href="https://docs.google.com/forms/d/e/1FAIpQLScU85kgbPmiIvhF3nuTBkpgXawRpTDbzRYWVcJZ0KvLFLEEcg/viewform?usp=publish-editor" target="_blank" rel="noopener noreferrer" className="inline-block bg-primary-container text-on-primary font-bold px-12 py-5 rounded-full electric-glow whitespace-nowrap hover:scale-105 transition-transform active:scale-95 text-lg">
    Join the Waitlist
</a>
</div>
<p className="mt-8 text-sm text-text-secondary">Be part of the exclusive group to experience premium EV mobility before the public launch.</p>
</div>
</div>
</div>
</section>

<section className="py-section-gap">
<div className="max-w-3xl mx-auto px-margin-mobile">
<h2 className="font-headline-xl text-headline-xl text-primary mb-12 text-center">Frequently Asked.</h2>
<div className="space-y-4">

<div className="glass-panel rounded-2xl overflow-hidden border border-glass-border">
<button className="faq-btn w-full p-6 text-left flex justify-between items-center group" >
<span className="text-primary font-bold">Why EV-only?</span>
<span className="material-symbols-outlined text-text-secondary transition-transform group-hover:text-primary">expand_more</span>
</button>
<div className="px-6 pb-6 text-text-secondary hidden">
                        Because partial solutions don't fix climate problems. By being 100% electric, we can optimize our infrastructure, reduce costs long-term, and guarantee every VoltRide is zero-emission.
                    </div>
</div>

<div className="glass-panel rounded-2xl overflow-hidden border border-glass-border">
<button className="faq-btn w-full p-6 text-left flex justify-between items-center group" >
<span className="text-primary font-bold">When are you launching?</span>
<span className="material-symbols-outlined text-text-secondary transition-transform group-hover:text-primary">expand_more</span>
</button>
<div className="px-6 pb-6 text-text-secondary hidden">
                        Phase 1 rollout begins in Bengaluru in Q4 2024. Waitlist members will receive priority access to the app before public release.
                    </div>
</div>

<div className="glass-panel rounded-2xl overflow-hidden border border-glass-border">
<button className="faq-btn w-full p-6 text-left flex justify-between items-center group" >
<span className="text-primary font-bold">How does Women Mode work?</span>
<span className="material-symbols-outlined text-text-secondary transition-transform group-hover:text-primary">expand_more</span>
</button>
<div className="px-6 pb-6 text-text-secondary hidden">
                        Once enabled in settings, you will only be matched with female drivers. Our system also prioritizes live tracking and faster SOS responses for these trips.
                    </div>
</div>
</div>
</div>
</section>

<section className="py-section-gap relative overflow-hidden">
<div className="absolute inset-0 bg-primary-container/5 opacity-50"></div>
<div className="max-w-container-max mx-auto px-margin-mobile md:px-gutter text-center relative z-10">
<h2 className="font-display-lg-mobile md:text-6xl text-primary font-bold mb-8">The Future Doesn't Run on Fuel.<br/><span className="text-primary-container">It Runs on Electricity.</span></h2>
<a href="https://docs.google.com/forms/d/e/1FAIpQLScU85kgbPmiIvhF3nuTBkpgXawRpTDbzRYWVcJZ0KvLFLEEcg/viewform?usp=publish-editor" target="_blank" rel="noopener noreferrer" className="inline-block px-12 py-6 bg-primary-container text-on-primary font-bold rounded-full electric-glow text-2xl transition-all hover:scale-110 active:scale-95">
                Join the Waitlist Now
            </a>
</div>
</section>

<footer className="bg-surface-container-lowest w-full py-section-gap border-t border-glass-border">
<div className="max-w-container-max mx-auto px-margin-mobile grid grid-cols-1 md:grid-cols-4 gap-stack-lg">
<div className="col-span-1 md:col-span-1">
<div className="font-headline-md text-headline-md text-primary font-bold mb-6">VoltRide</div>
<p className="text-text-secondary text-sm leading-relaxed">Pioneering sustainable urban mobility for a cleaner, safer Bengaluru.</p>
</div>
<div>
<h4 className="text-primary font-bold mb-6 uppercase text-xs tracking-widest">Platform</h4>
<ul className="space-y-4">
<li><a className="text-secondary hover:text-primary-fixed-dim transition-colors" href="#">Models</a></li>
<li><a className="text-secondary hover:text-primary-fixed-dim transition-colors" href="#">Women Mode</a></li>
<li><a className="text-secondary hover:text-primary-fixed-dim transition-colors" href="#">Driver Partners</a></li>
</ul>
</div>
<div>
<h4 className="text-primary font-bold mb-6 uppercase text-xs tracking-widest">Resources</h4>
<ul className="space-y-4">
<li><a className="text-secondary hover:text-primary-fixed-dim transition-colors" href="#">Privacy Policy</a></li>
<li><a className="text-secondary hover:text-primary-fixed-dim transition-colors" href="#">Terms of Service</a></li>
<li><a className="text-secondary hover:text-primary-fixed-dim transition-colors" href="#">Carbon Reports</a></li>
</ul>
</div>
<div>
<h4 className="text-primary font-bold mb-6 uppercase text-xs tracking-widest">Connect</h4>
<div className="flex gap-4">
<a className="w-10 h-10 rounded-full border border-glass-border flex items-center justify-center hover:bg-primary-container hover:text-on-primary transition-all" href="#">
<span className="material-symbols-outlined text-sm">alternate_email</span>
</a>
<a className="w-10 h-10 rounded-full border border-glass-border flex items-center justify-center hover:bg-primary-container hover:text-on-primary transition-all" href="#">
<span className="material-symbols-outlined text-sm">public</span>
</a>
</div>
</div>
</div>
<div className="max-w-container-max mx-auto px-margin-mobile mt-16 pt-8 border-t border-glass-border flex flex-col md:flex-row justify-between items-center text-text-secondary text-xs gap-4">
<p>© 2024 VoltRide Mobility. Crafted for Bengaluru.</p>
<div className="flex gap-6">
<span>Bengaluru Operations</span>
<span>Waitlist Status: OPEN</span>
</div>
</div>
</footer>

    </div>
  );
}
