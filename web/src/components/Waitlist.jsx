import { motion } from "framer-motion";
import { ArrowRight } from "lucide-react";

export default function Waitlist() {
  return (
    <section id="waitlist" className="py-48 relative overflow-hidden bg-slate-50">
      <div className="absolute inset-0 bg-[radial-gradient(ellipse_at_top,_var(--tw-gradient-stops))] from-primary-container/20 via-background to-background"></div>
      
      <div className="max-w-container-max mx-auto px-margin-mobile md:px-gutter relative z-10">
        <motion.div 
          initial={{ opacity: 0, scale: 0.95 }}
          whileInView={{ opacity: 1, scale: 1 }}
          viewport={{ once: true }}
          transition={{ duration: 1, ease: [0.16, 1, 0.3, 1] }}
          className="glass-panel p-12 md:p-32 rounded-[4rem] text-center relative overflow-hidden border border-primary-container/30 bg-surface-container-lowest/50 backdrop-blur-3xl shadow-2xl"
        >
          <div className="relative z-10 max-w-4xl mx-auto flex flex-col items-center">
            <div className="mb-10 inline-block bg-primary-container/10 border border-primary-container/30 rounded-full px-6 py-2 text-primary-container font-bold tracking-widest text-sm uppercase shadow-[0_0_20px_rgba(77,168,255,0.1)]">
              Launching First in Bengaluru, Karnataka 🇮🇳
            </div>
            
            <h2 className="font-display-lg-mobile md:font-display-lg text-[10vw] md:text-[5vw] leading-[1.1] tracking-tighter text-primary mb-8">
              Help shape the future of <span className="gradient-text italic">sustainable mobility.</span>
            </h2>
            
            <p className="text-xl md:text-2xl text-text-secondary mb-16 font-light">Join the early access list today.</p>
            
            <a 
              href="https://docs.google.com/forms/d/e/1FAIpQLScU85kgbPmiIvhF3nuTBkpgXawRpTDbzRYWVcJZ0KvLFLEEcg/viewform?usp=publish-editor" 
              target="_blank" 
              rel="noopener noreferrer" 
              className="group inline-flex items-center gap-4 bg-primary-container text-on-primary font-bold px-12 py-6 rounded-full electric-glow whitespace-nowrap hover:scale-105 transition-all active:scale-95 text-xl md:text-2xl"
            >
              Join the Waitlist
              <ArrowRight className="group-hover:translate-x-2 transition-transform" />
            </a>
            
            <p className="mt-12 text-sm text-text-secondary tracking-widest uppercase">
              Be part of the exclusive group to experience premium EV mobility before the public launch.
            </p>
          </div>
        </motion.div>
      </div>
    </section>
  );
}
